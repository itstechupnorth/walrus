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
package org.itstechupnorth.walrus.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.ConcurrentModificationException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.lang.CharUtils;

public class ArticleBuffer {

    private static final String NAME_SUFFIX = ".txt";

    private static final String NAME_PREFIX = "article-";

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static AtomicInteger instanceNumber = new AtomicInteger(0);

    public static final int K = 1024;

    private static final int OUT_BUFFER_CAPACITY = 8 * K;
    public static final int M = 1024 * K;
    public static final int DEFAULT_BUFFER = 8 * M;

    private StringBuilder title = new StringBuilder();
    private final CharBuffer buffer;
    private final ByteBuffer outBuffer;
    private final CharsetEncoder encoder;
    private final int number;

    private volatile boolean saving = false;

    public ArticleBuffer() {
        this(DEFAULT_BUFFER);
        // System.out.println(moniker() + "new");
    }

    public ArticleBuffer(final int bufferSize) {
        super();
        buffer = CharBuffer.allocate(bufferSize);
        this.number = instanceNumber.getAndIncrement();
        outBuffer = ByteBuffer.allocate(OUT_BUFFER_CAPACITY);
        encoder = UTF8.newEncoder();
    }

    public void reset() {
        buffer.clear();
        title = new StringBuilder();
        // / System.out.println(moniker() + "reset");
    }

    private String moniker() {
        return "BUFFER[" + number + "] ";
    }

    public String getTitleHash() {
        return DigestUtils.md5Hex(getTitle());
    }

    public String getTitle() {
        return title.toString().trim();
    }

    public ArticleBuffer title(char[] ch, int start, int length) {
        if (saving)
            throw new ConcurrentModificationException();
        title.append(ch, start, length);
        return this;
    }

    public ArticleBuffer text(char[] ch, int start, int length)
            throws BufferOverflowException {
        if (saving)
            throw new ConcurrentModificationException();
        // System.out.println(moniker() + "text@" + buffer.position());
        buffer.put(ch, start, length);
        return this;
    }

    // public void parse() {
    // BasicWikiParser parser = new BasicWikiParser(CharBuffer.wrap(buffer));
    // //BasicWikiParser parser = new
    // TitleTrackingWikiParser(CharBuffer.wrap(buffer));
    // parser.parse();
    // }

    @Override
    public String toString() {
        return moniker() + " (title=" + getTitle() + ")";
    }

    public void logTo(PrintStream out) {
        StringBuilder builder = new StringBuilder("number=");
        builder.append(number);
        builder.append(" title=");
        builder.append(getTitle());
        builder.append(" md5=");
        builder.append(getTitleHash());
        out.println(builder);
    }

    public ArticleBuffer feed(CharProcessor processor) throws IOException {
        saving = true;
        final int originalPosition = buffer.position();
        try {
            buffer.flip();
            while (buffer.hasRemaining()) {
                processor.process(buffer.get());
            }
        } finally {
            buffer.clear();
            buffer.position(originalPosition);
            saving = false;
        }
        return this;
    }

    public ArticleBuffer saveTo(File file) throws IOException {
        final FileOutputStream out = new FileOutputStream(file);
        saveTo(out.getChannel()).close();
        out.close();
        return this;
    }

    public WritableByteChannel saveTo(WritableByteChannel out)
            throws IOException {
        saving = true;
        final int originalPosition = buffer.position();
        // System.out.println(moniker() + "saving@" + buffer.position());
        try {
            buffer.flip();
            encoder.reset();

            outBuffer.clear();
            boolean more = true;
            while (more) {
                final CoderResult result = encoder.encode(buffer, outBuffer,
                        true);
                outBuffer.flip();
                out.write(outBuffer);
                outBuffer.clear();
                more = CoderResult.OVERFLOW.equals(result);
            }
            return out;
        } finally {
            buffer.clear();
            buffer.position(originalPosition);
            // System.out.println(moniker() + "saved@" + buffer.position());
            saving = false;
        }
    }

    public void saveTo(ZipArchiveOutputStream out) throws IOException {
        saving = true;
        final int originalPosition = buffer.position();
        // System.out.println(moniker() + "saving@" + buffer.position());
        try {
            buffer.flip();
            encoder.reset();
            final String name = NAME_PREFIX + getTitleHash() + NAME_SUFFIX;
            final ZipArchiveEntry entry = new ZipArchiveEntry(name);
            entry.setComment(getTitle());
            out.putArchiveEntry(entry);

            outBuffer.clear();
            boolean more = true;
            while (more) {
                final CoderResult result = encoder.encode(buffer, outBuffer,
                        true);
                out.write(outBuffer.array(), 0, outBuffer.position());
                outBuffer.clear();
                more = CoderResult.OVERFLOW.equals(result);
            }

            out.closeArchiveEntry();
        } finally {
            buffer.clear();
            buffer.position(originalPosition);
            // System.out.println(moniker() + "saved@" + buffer.position());
            saving = false;
        }
    }

    public CharBuffer toBuffer() {
        return (CharBuffer) buffer.asReadOnlyBuffer().flip();
    }

    public void setTitle(String title) {
        this.title = new StringBuilder(title);
    }

    public void textLine(String line) {
        buffer.append(line);
        buffer.append(CharUtils.CR);
        buffer.append(CharUtils.LF);
    }

    public void read(Reader in) throws IOException {
        int read = 0;
        while (read >= 0) {
            read = in
                    .read(buffer.array(), buffer.position(), buffer.capacity());
            buffer.position(read);
        }
    }
}
