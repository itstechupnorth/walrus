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
package org.itstechupnorth.walrus.app;

import java.io.File;

import org.itstechupnorth.walrus.base.Constants;
import org.itstechupnorth.walrus.dir.DirectoryProcessor;
import org.itstechupnorth.walrus.zip.Archive;


public class Walrus {

    public enum Act {
        ARCHIVE, LOG, COUNT
    };

    public enum Subject {
        WIKI, ARCHIVE, DETECT, DIRECTORY
    };

    private String path = null;

    private String outputFile = null;

    private Act act = Act.LOG;

    private Subject subject = Subject.DETECT;

    private int buffers = Constants.DEFAULT_NUMBER_OF_BUFFERS;

    private int threads = Constants.DEFAULT_NUMBER_OF_THREADS;

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public int getBuffers() {
        return buffers;
    }

    public void setBuffers(int buffers) {
        this.buffers = buffers;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void runWalrus() throws Exception {
        System.out.println("Walrus " + Constants.VERSION);
        System.out.println(this);
        switch (act) {
            case ARCHIVE:
                archive();
                break;
            case LOG:
                log();
                break;
            case COUNT:
                count();
                break;
        }
    }

    private void count() throws Exception {
        switch (subject) {
            case DETECT:
                detect();
                count();
                break;
            case DIRECTORY:
                countDirectory();
                break;
            default:
                reportUnknownSubject();
        }
    }

    public void log() throws Exception {
        if (path == null)
            throw new RuntimeException("Path required");
        if (!new File(path).exists())
            throw new RuntimeException("File does not exist " + path);
        switch (subject) {
            case WIKI:
                logWiki();
                break;
            case ARCHIVE:
                logArchive();
                break;
            case DETECT:
                detect();
                log();
                break;
            case DIRECTORY:
                logDirectory();
                break;
            default:
                reportUnknownSubject();
        }
    }

    private void reportUnknownSubject() {
        throw new RuntimeException("Unknown subject");
    }

    private void countDirectory() throws Exception {
        processDirectory(DirectoryProcessor.Act.COUNT);
    }

    private void logDirectory() throws Exception {
        processDirectory(DirectoryProcessor.Act.LOG);
    }

    private void processDirectory(final DirectoryProcessor.Act act)
            throws Exception {
        new DirectoryProcessor(path).setThreads(threads).setBuffers(buffers)
                .setOutputFile(outputFile).setAction(act).perform();
    }

    private void detect() {
        if (Archive.isArchive(path)) {
            subject = Subject.ARCHIVE;
        } else if (new File(path).isDirectory()) {
            subject = Subject.DIRECTORY;
        } else {
            subject = Subject.WIKI;
        }
    }

    private void logArchive() throws Exception {
        logArchive(path);
    }

    public void archive() throws Exception {
        if (path == null)
            throw new RuntimeException("Path required");
        switch (subject) {
            case WIKI:
            case DETECT:
                archiveWiki();
                break;
            default:
                reportUnknownSubject();
        }
    }

    public void archiveWiki() throws Exception {
        archiveWiki(path);
    }

    public void archiveWiki(final String path) throws Exception {
        new ArchiveWiki(path).setThreads(threads).setBuffers(buffers).perform();
    }

    public void logWiki() throws Exception {
        logWiki(path);
    }

    public void logWiki(final String path) throws Exception {
        new LogWiki(path).perform();
    }

    public void logArchive(final String path) throws Exception {
        Archive archive = new Archive(new File(path));
        System.out.println(archive.open().articles());
    }

    @Override
    public String toString() {
        return "Walrus [act=" + act + ", buffers=" + buffers + ", path=" + path
                + ", subject=" + subject + ", threads=" + threads + "]";
    }
}
