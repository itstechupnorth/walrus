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

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.zip.ZipException;

import org.itstechupnorth.walrus.base.ArticleBuffer;

import name.robertburrelldonkin.alfie.Actor;
import name.robertburrelldonkin.alfie.ActorFactory;

public class GutenburgWordsActor implements Actor<File, WordCount> {

    public static final class Factory implements ActorFactory<File, WordCount> {

        public Actor<File, WordCount> build() {
            return pooling();
        }

    }

    public static GutenburgWordsActor pooling() {
        return new GutenburgWordsActor(System.out, new ArticleBuffer());
    }

    private final PrintStream out;
    private final ArticleBuffer buffer;

    public GutenburgWordsActor() {
        this(System.out, null);
    }

    private GutenburgWordsActor(final PrintStream out,
            final ArticleBuffer buffer) {
        this.out = out;
        this.buffer = buffer;
    }

    public void printCount(final File file) throws IOException {
        count(file).printTo(out);
    }

    private WordCounter count(final File file) throws IOException {
        final List<String> words = words(file);
        WordCounter counter = new WordCounter();
        counter.setIgnoreCase(true).load(words);
        return counter;
    }

    private List<String> words(final File file) throws IOException {
        final GutenburgParser parser = new GutenburgParser(file);
        final SimpleWordParser wordParser = new SimpleWordParser();
        parser.setBuffer(buffer).read().feed(wordParser);
        final List<String> words = wordParser.getWords();
        return words;
    }

    public WordCount act(File subject) throws Exception {
        // System.out.println("[GUTEN] Start");
        try {
            return count(subject).getCount();
        } catch (ZipException e) {
            System.out.println("[FAIL] Cannot unzip " + subject);
            throw e;
        } catch (InvalidFormatException e) {
            System.out.println("[FAIL] Invalid format " + subject);
            return EmptyWordCount.INSTANCE;
        } finally {
            // System.out.println("[GUTEN] End");
        }
    }

    public void finish() {
    }
}
