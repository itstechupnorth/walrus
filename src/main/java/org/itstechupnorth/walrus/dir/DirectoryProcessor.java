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
package org.itstechupnorth.walrus.dir;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import name.robertburrelldonkin.alfie.Actor;
import name.robertburrelldonkin.alfie.Conductor;
import name.robertburrelldonkin.alfie.Engine;
import name.robertburrelldonkin.alfie.LogActor;
import name.robertburrelldonkin.alfie.Message;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.itstechupnorth.walrus.base.Constants;
import org.itstechupnorth.walrus.text.TextBuilder;


public class DirectoryProcessor {

    public enum Act {
        LOG, COUNT
    }

    private final String pathname;

    private int buffers = Constants.DEFAULT_NUMBER_OF_BUFFERS;
    private int threads = Constants.DEFAULT_NUMBER_OF_THREADS;

    private String outputFile = null;

    private Act action = Act.LOG;

    private boolean ignoreCase = true;

    public DirectoryProcessor(String pathname) {
        super();
        this.pathname = pathname;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public DirectoryProcessor setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        return this;

    }

    public String getOutputFile() {
        return outputFile;
    }

    public DirectoryProcessor setOutputFile(String outputFile) {
        this.outputFile = outputFile;
        return this;
    }

    public Act getAction() {
        return action;
    }

    public DirectoryProcessor setAction(Act action) {
        this.action = action;
        return this;
    }

    public int getBuffers() {
        return buffers;
    }

    public DirectoryProcessor setBuffers(int buffers) {
        this.buffers = buffers;
        return this;
    }

    public int getThreads() {
        return threads;
    }

    public DirectoryProcessor setThreads(int threads) {
        this.threads = threads;
        return this;
    }

    public void perform() throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("Started " + now());

        final BlockingQueue<Message<File>> queue = queue();
        final Conductor conductor = new Conductor(threads, new DirectorySource(
                queue).setFilter(filter()).setFile(new File(pathname)));
        fill(conductor, queue).start().perform().finish();
        long end = System.currentTimeMillis();

        System.out.println("Ended " + now());
        System.out.println("Duration " + (end - start) / 1000f + " (secs)");
    }

    private Conductor fill(Conductor conductor,
            BlockingQueue<Message<File>> queue) {
        switch (action) {
            case LOG:
                return logEngine(conductor, queue);
            case COUNT:
                return new TextBuilder().setBuffers(buffers)
                        .setIgnoreCase(ignoreCase).setOutputFile(outputFile)
                        .countEngine(conductor, queue);
            default:
                throw new RuntimeException("Not supported");
        }

    }

    private Conductor logEngine(Conductor conductor,
            final BlockingQueue<Message<File>> queue) {
        final Actor<File, File> actor = new LogActor<File>();
        final Engine<File, File> engine = new Engine<File, File>(queue,
                conductor.getExecutor(), actor);
        conductor.manage(engine);
        return conductor;
    }

    protected FileFilter filter() {
        return new SuffixFileFilter(".zip");
    }

    protected BlockingQueue<Message<File>> queue() {
        return new ArrayBlockingQueue<Message<File>>(buffers);
    }

    @SuppressWarnings("deprecation")
    private String now() {
        return new Date().toGMTString();
    }
}
