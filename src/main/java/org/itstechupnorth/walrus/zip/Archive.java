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
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.itstechupnorth.walrus.base.ArticleBuffer;
import org.itstechupnorth.walrus.base.Constants;

public class Archive {

    public static boolean isArchive(String path) {
        try {
            new Archive(new File(path)).open();
            return true;
        } catch (IOException e) {
            // Probably not an archive
        }
        return false;
    }

    private static final int DEFAULT_CAPACITY = 128;
    private final File file;
    private ZipFile zip;

    public Archive(File archive) {
        super();
        this.file = archive;
    }

    public Archive open() throws IOException {
        zip = new ZipFile(this.file);
        return this;
    }

    @SuppressWarnings("unchecked")
    public List<String> titles() {
        final List<String> results = new ArrayList<String>(DEFAULT_CAPACITY);
        for (Enumeration<ZipArchiveEntry> en = zip.getEntries(); en
                .hasMoreElements();) {
            results.add(en.nextElement().getComment());
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    public List<Article> articles() {
        final List<Article> results = new ArrayList<Article>(DEFAULT_CAPACITY);
        for (Enumeration<ZipArchiveEntry> en = zip.getEntries(); en
                .hasMoreElements();) {
            results.add(new Article(en.nextElement(), zip));
        }
        return results;
    }

    public static class Article {
        private final ZipFile zip;
        private final ZipArchiveEntry entry;
        private final String title;

        public Article(final ZipArchiveEntry entry, final ZipFile zip) {
            super();
            this.entry = entry;
            this.zip = zip;
            title = entry.getComment();
        }

        public String getTitle() {
            return title;
        }

        public void fill(final ArticleBuffer buffer) throws IOException {
            buffer.reset();
            buffer.setTitle(title);

            final Reader in = new InputStreamReader(zip.getInputStream(entry),
                    Constants.UTF_8);
            buffer.read(in);
        }

        @Override
        public String toString() {
            return "Article [title=" + title + "]";
        }
    }
}
