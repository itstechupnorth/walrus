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
import java.util.concurrent.BlockingQueue;

import org.itstechupnorth.walrus.base.Constants;

import name.robertburrelldonkin.alfie.Actor;
import name.robertburrelldonkin.alfie.Conductor;
import name.robertburrelldonkin.alfie.Message;
import name.robertburrelldonkin.alfie.Pool;
import name.robertburrelldonkin.alfie.builders.EngineSinkBuilder;

public class TextBuilder {

    private String outputFile = null;
    private int buffers = Constants.DEFAULT_NUMBER_OF_BUFFERS;
    private boolean ignoreCase = true;
    private boolean small = false;

    public TextBuilder() {
    }

    public int getBuffers() {
        return buffers;
    }

    public TextBuilder setBuffers(int buffers) {
        this.buffers = buffers;
        return this;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public TextBuilder setOutputFile(String outputFile) {
        this.outputFile = outputFile;
        return this;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public TextBuilder setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        return this;
    }

    public Conductor countEngine(Conductor conductor,
            BlockingQueue<Message<File>> fromSource) {
        if (small) {
            return smallCountEngine(conductor, fromSource);
        }
        return scalableCountEngine(conductor, fromSource);
    }

    private Conductor scalableCountEngine(Conductor conductor,
            BlockingQueue<Message<File>> fromSource) {
        final Actor<File, WordCount> actor = new Pool<File, WordCount>(
                new GutenburgWordsActor.Factory(), buffers).open();
        // final Actor<List<String>> sinkActor = new LogActor<List<String>>();
        final Actor<WordCount, WordCount> sinkActor = buildWordCounter();
        return new EngineSinkBuilder<File, WordCount>().build(conductor,
                fromSource, actor, sinkActor);
    }

    private Conductor smallCountEngine(Conductor conductor,
            BlockingQueue<Message<File>> fromSource) {
        final Actor<File, WordCount> actor = new Pool<File, WordCount>(
                new GutenburgWordsActor.Factory(), buffers).open();
        // final Actor<List<String>> sinkActor = new LogActor<List<String>>();
        final Actor<WordCount, WordCount> sinkActor = buildWordCounter();
        return new EngineSinkBuilder<File, WordCount>().build(conductor,
                fromSource, actor, sinkActor);
    }

    private WordCounterActor buildWordCounter() {
        final WordCounterActor wordCounter = new WordCounterActor();
        if (outputFile != null) {
            wordCounter.setOutputFile(new File(outputFile));
        }
        wordCounter.setIgnoreCase(ignoreCase);
        return wordCounter;
    }

}
