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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class WordCounter {

    private final WordCount count = new MapWordCount();

    private boolean ignoreCase = false;

    private boolean ignoreNumerals = true;

    private PrintStream out = System.out;

    private File outputFile = null;

    public boolean isIgnoreNumerals() {
        return ignoreNumerals;
    }

    public void setIgnoreNumerals(boolean ignoreNumerals) {
        this.ignoreNumerals = ignoreNumerals;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public WordCounter setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        return this;
    }

    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }

    public WordCounter load(List<String> words) {
        count.clear();
        add(words);
        return this;
    }

    private void add(List<String> words) {
        // System.out.println("[COUNT] Start");
        // System.out.println("[COUNT] words " + words.size());
        for (String word : words) {
            if (ignoreCase) {
                word = word.toLowerCase();
            }
            if (ignoreNumerals && isNumeric(word)) {
                // ignore
                // System.out.println("[IGNORING] " + word);
            } else {
                count.inc(word);
            }
        }
        // System.out.println("[COUNT] End");
    }

    private boolean isNumeric(String string) {
        return isIntegral(string) || isDecimal(string);
    }

    private boolean isIntegral(String string) {
        try {
            Long.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDecimal(String string) {
        try {
            Double.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public WordCounter printTo(PrintStream out) {
        out.println("# words: " + count.size());
        int i = 0;
        WordCount.Walk walk = count.descending();
        while (walk.hasNext()) {
            if (++i % 100 == 0)
                out.println("# @" + i);
            out.println(walk.next() + " " + walk.count());
        }
        out.println("#");
        out.println("# Just Words");
        out.println("#");
        i = 0;
        walk = count.descending();
        while (walk.hasNext()) {
            if (++i % 100 == 0)
                out.println("# @" + i);
            out.println(walk.next());
        }
        out.flush();
        return this;
    }

    public List<String> act(List<String> words) throws Exception {
        add(words);
        return words;
    }

    public WordCount act(WordCount wordCount) throws Exception {
        count.merge(wordCount);
        return wordCount;
    }

    public WordCount getCount() {
        return count;
    }

    public void finish() {
        System.out.println("[RESULTS] Printing...");
        boolean closeStream = false;
        if (outputFile != null) {
            try {
                out = new PrintStream(new BufferedOutputStream(
                        new FileOutputStream(outputFile)));
                closeStream = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                out = null;
            }
        }
        if (out == null) {
            out = System.out;
        }
        printTo(out);
        System.out.println("[RESULTS] Done.");
        if (closeStream) {
            IOUtils.closeQuietly(out);
        }
    }
}
