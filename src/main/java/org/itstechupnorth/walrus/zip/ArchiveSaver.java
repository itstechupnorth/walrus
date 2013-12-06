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
package org.itstechupnorth.walrus.zip;

import java.io.File;
import java.io.IOException;
import java.util.zip.Deflater;

import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.itstechupnorth.walrus.base.ArticleBuffer;


public class ArchiveSaver {

    private final File archive;

    private ZipArchiveOutputStream out;

    private String comment = "Walrus Archive";

    private int level = Deflater.BEST_COMPRESSION;

    private boolean open = false;

    public ArchiveSaver(File archive) {
        super();
        this.archive = archive;
    }

    public ArchiveSaver(String path) {
        this(new File(path));
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public synchronized void open() throws IOException {
        if (!open) {
            open = true;
            out = new ZipArchiveOutputStream(archive);
            out.setLevel(level);
            out.setFallbackToUTF8(true);
        }
    }

    public boolean isOpen() {
        return open;
    }

    public synchronized void save(ArticleBuffer buffer) throws IOException {
        open();
        buffer.saveTo(out);
        // System.out.println(buffer.getTitle());
    }

    public synchronized void close() throws IOException {
        if (open) {
            out.setComment(comment);
            out.close();
            open = false;
        }
    }

    @Override
    public String toString() {
        return "ArchiveSaver [archive=" + archive + ", comment=" + comment
                + ", level=" + level + ", open=" + open + "]";
    }
}
