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
import java.util.concurrent.BlockingQueue;

import name.robertburrelldonkin.alfie.Message;
import name.robertburrelldonkin.alfie.QueuingSource;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class DirectorySource extends QueuingSource<File> {

    private FileFilter filter = TrueFileFilter.TRUE;
    private File file;

    public DirectorySource(final BlockingQueue<Message<File>> out) {
        super(out);
    }

    public FileFilter getFilter() {
        return filter;
    }

    public DirectorySource setFilter(FileFilter filter) {
        this.filter = filter;
        return this;
    }

    public void walk(final File directory) {
        if (directory.isDirectory()) {
            for (final File file : directory.listFiles(filter)) {
                output(file);
            }
            for (final File file : directory
                    .listFiles((FileFilter) DirectoryFileFilter.INSTANCE)) {
                walk(file);
            }
        } else {
            output(directory);
        }
    }

    public File getFile() {
        return file;
    }

    public DirectorySource setFile(File file) {
        this.file = file;
        return this;
    }

    public void open() {
        walk(file);
    }
}
