/*
 *  Copyright 2010-2013 Robert Burrell Donkin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.itstechupnorth.walrus.text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipException;

import name.robertburrelldonkin.alfie.RuntimeWarningException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FilenameUtils;
import org.itstechupnorth.walrus.base.ArticleBuffer;


public class GutenburgParser {

    private static final String ENDINGS[] = {
            "End of Project Gutenberg".toLowerCase(),
            "*** END OF THIS PROJECT GUTENBERG EBOOK".toLowerCase(),
            "***END OF THE PROJECT GUTENBERG EBOOK".toLowerCase(),
            "*** END OF THE PROJECT GUTENBERG EBOOK".toLowerCase(),
            "End of Project Gutenberg's Etext".toLowerCase(),
            "End of The Project Gutenberg Etext".toLowerCase(),
            "ETEXT EDITOR".toLowerCase(),
            " *** END OF THIS PROJECT GUTENBERG EBOOK".toLowerCase(),
            "Project Gutenberg EBook".toLowerCase(),
            "Project Gutenberg Etext".toLowerCase(), };
    private static final String STARTINGS[] = {
            "*END*THE SMALL PRINT! FOR PUBLIC DOMAIN ETEXTS".toLowerCase(),
            "*END THE SMALL PRINT! FOR PUBLIC DOMAIN ETEXTS".toLowerCase(),
            "*** START OF THIS PROJECT GUTENBERG EBOOK".toLowerCase(),
            "***START OF THE PROJECT GUTENBERG EBOOK".toLowerCase(),
            "*** START OF THE PROJECT GUTENBERG EBOOK".toLowerCase(),
            "**END THE SMALL PRINT! FOR PUBLIC DOMAIN ETEXTS".toLowerCase(),
            "This etext was produced by".toLowerCase(),
            " *** START OF THIS PROJECT GUTENBERG EBOOK".toLowerCase() };
    private static final String[] SIGNATURES = { "EBook of".toLowerCase(),
            "Etext of".toLowerCase(), "Project Gutenberg's".toLowerCase(),
            "The Project Gutenberg eBook".toLowerCase(),
            "Project Gutenberg Etext".toLowerCase(),
            "PROJECT GUTENBERG EBOOK".toLowerCase() };
    private static final int[] SIGNATURE_LENGTHS = { SIGNATURES[0].length(),
            SIGNATURES[1].length(), SIGNATURES[2].length(),
            SIGNATURES[3].length(), SIGNATURES[4].length() };

    private static final String ZIP = "zip";

    private final File file;

    private ArticleBuffer buffer = null;

    public GutenburgParser(String fileName) {
        this(new File(fileName));
    }

    public GutenburgParser(final File file) {
        this.file = file;
    }

    public ArticleBuffer getBuffer() {
        return buffer;
    }

    public GutenburgParser setBuffer(ArticleBuffer buffer) {
        this.buffer = buffer;
        return this;
    }

    public ArticleBuffer read() throws IOException {
        final ArticleBuffer buffer;
        if (this.buffer == null) {
            buffer = new ArticleBuffer();
        } else {
            buffer = this.buffer;
        }
        return read(buffer);
    }

    private ArticleBuffer read(final ArticleBuffer buffer) throws IOException,
            ZipException, FileNotFoundException, UnsupportedEncodingException {
        final BufferedReader reader = new BufferedReader(open());
        try {
            final String firstLine = firstLine(reader);
            if (firstLine == null) {
                throw new RuntimeWarningException("[WARN] Empty");
            } else {
                final String title = title(firstLine);
                // System.out.println("[TITLE] '" + title + "'");
                buffer.setTitle(title);
                skipPrologue(reader);
                readMain(reader, buffer);
            }
            return buffer;

        } finally {
            reader.close();
        }
    }

    private String firstLine(final BufferedReader reader) throws IOException {
        final String result;
        final String line = reader.readLine();
        if (line == null) {
            result = null;
        } else if ("".equals(line.trim())) {
            result = firstLine(reader);
        } else {
            result = line;
        }
        return result;
    }

    private String title(final String firstLine) throws IOException {
        if (firstLine != null && firstLine.contains("DOCTYPE HTML PUBLIC")) {
            throw new InvalidFormatException("[WARN] HTML is unsupported.");
        }
        String title = null;
        for (int i = 0; i < SIGNATURES.length; i++) {
            title = afterSignature(firstLine, SIGNATURE_LENGTHS[i],
                    SIGNATURES[i]);
            if (title != null) {
                break;
            }
        }
        if (title == null) {
            throw new RuntimeWarningException("[WARN] Unexpected first line: "
                    + firstLine);
        }
        if (title.startsWith(",")) {
            title = title.substring(1);
        }
        return title.trim();
    }

    private String afterSignature(final String firstLine,
            final int signatureLength, final String signature) {
        final int signatureIndex = firstLine.toLowerCase().indexOf(signature);
        final String title;
        if (signatureIndex >= 0) {
            title = firstLine.substring(signatureIndex + signatureLength)
                    .trim();
        } else {
            title = null;
        }
        return title;
    }

    private void readMain(final BufferedReader reader,
            final ArticleBuffer buffer) throws IOException {
        int lineCount = 0;
        boolean main = true;
        boolean note = false;
        while (main) {
            lineCount++;
            final String line = firstLine(reader);
            if (isEnding(line)) {
                main = false;
            } else {
                if (line.startsWith("[")) {
                    note = true;
                }
                if (line.contains("]")) {
                    note = false;
                }
                if (note) {
                    // System.out.println("[NOTE] " + line);
                } else {
                    if (line.contains("Project Gutenberg")
                            || line.contains("PROJECT GUTENBERG"))
                        throw new RuntimeWarningException(
                                "[WARN] Missed ending #" + lineCount + " '"
                                        + line + "'");
                    buffer.textLine(line);
                }
            }
        }
    }

    private boolean isEnding(String line) {
        if (line == null)
            return true;
        line = line.toLowerCase();
        for (String end : ENDINGS) {
            if (line.contains(end)) {
                return true;
            }
        }
        return false;
    }

    private void skipPrologue(final BufferedReader reader) throws IOException {
        boolean prologue = true;
        while (prologue) {
            final String line = firstLine(reader);
            // System.out.println(line);
            if (isStarting(line)) {
                prologue = false;
            }
        }
        boolean note = false;
        for (int i = 1; i < 8; i++) {
            // @SuppressWarnings("unused")
            final String line = firstLine(reader);
            if (line == null) {
                throw new RuntimeWarningException(
                        "[WARN] Premature end to prologue.");
                // break;
            }
            if (line.startsWith("[")) {
                note = true;
            }
            // if (note) System.out.println("[NOTE] " + line);
            if (line.contains("]")) {
                note = false;
            }
            // System.out.println("[SKIP] " + line);
        }
        if (note) {
            while (note) {
                final String line = firstLine(reader);
                // System.out.println("[NOTE] " + line);
                if (line.contains("]")) {
                    note = false;
                }
            }
        }
    }

    private boolean isStarting(String line) {
        if (line == null)
            return true;
        line = line.toLowerCase();
        for (String starting : STARTINGS) {
            if (line.startsWith(starting)) {
                return true;
            }
        }
        return false;
    }

    private Reader open() throws IOException, ZipException,
            FileNotFoundException, UnsupportedEncodingException {
        final Reader reader;
        final InputStream input;
        if (FilenameUtils.isExtension(file.getName(), ZIP)) {
            try {
                final ZipFile zip = new ZipFile(file);
                input = zip.getInputStream((ZipArchiveEntry) zip.getEntries()
                        .nextElement());
            } catch (IllegalArgumentException e) {
                throw new RuntimeWarningException(e.getMessage() + "(" + file
                        + ")", e);
            }
        } else {
            input = new FileInputStream(file);
        }
        reader = new InputStreamReader(new BufferedInputStream(input), "UTF-8");
        return reader;
    }

    public void saveText() throws IOException {
        final File file = new File(this.file.getParentFile(),
                FilenameUtils.getBaseName(this.file.getName()) + ".txt");
        read().saveTo(file);
    }

}
