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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.CharBuffer;

public class WordCountApp {

    private static final String TRANSCRIPT_FILE = "TEXT.txt";

    public static void main(String[] args) throws Exception {

        WordCountApp app = new WordCountApp();
        String directory = "directory";
        app.read(directory);
        app.print(System.out);
    }

    private void print(PrintStream out) {
        WordCounter counter = new WordCounter();
        counter.setIgnoreCase(true);
        counter.load(parser.getWords()).printTo(out);
    }

    private final CharBuffer buffer = CharBuffer.allocate(2048);
    private final SimpleWordParser parser = new SimpleWordParser();

    public void read(String name) throws IOException {
        read(new File(name));
    }

    public void read(final File file) throws IOException {
        if (file.isDirectory()) {
            for (File childFile : file.listFiles()) {
                if (childFile.equals(file.getParentFile())) {
                    System.out.println("Ignoring circular directory");
                } else {
                    read(childFile);
                }
            }
        } else if (TRANSCRIPT_FILE.equals(file.getName())) {
            Reader reader = new InputStreamReader(new BufferedInputStream(
                    new FileInputStream(file)), "UTF-8");
            readAll(reader);
        } else {
            // System.out.println("Ignoring " + file);
        }
    }

    public void readAll(Reader reader) throws IOException {
        // parser.clear();

        while (read(reader))
            ;
        parser.eof();
    }

    private boolean read(Reader reader) throws IOException {
        buffer.clear();
        int read = reader.read(buffer);
        buffer.flip();
        for (int i = 0; i < read; i++) {
            final char next = buffer.get();
            parser.process(next);
        }
        return read >= 0;
    }
}
