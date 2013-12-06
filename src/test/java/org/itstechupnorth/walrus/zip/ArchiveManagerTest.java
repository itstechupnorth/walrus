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
import java.util.Enumeration;

import junit.framework.TestCase;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.itstechupnorth.walrus.base.ArticleBuffer;
import org.itstechupnorth.walrus.zip.ArchiveSaver;


public class ArchiveManagerTest extends TestCase {

    File parent;

    @Override
    protected void setUp() throws Exception {
        parent = new File("target/tests/");
        parent.mkdirs();
    }

    public void testEmpty() throws Exception {

        ArchiveSaver manager = new ArchiveSaver(file("test.zip"));
        manager.open();
        manager.close();
    }

    public void testOneWrite() throws Exception {

        ArchiveSaver manager = new ArchiveSaver(file("write.zip"));
        manager.open();
        ArticleBuffer buffer = new ArticleBuffer();
        buffer.title(new String("Hello").toCharArray(), 0, 5);
        buffer.text(new String("World").toCharArray(), 0, 5);
        manager.save(buffer);
        manager.close();
    }

    public void testOneRound() throws Exception {

        final File file = file("round.zip");
        ArchiveSaver manager = new ArchiveSaver(file);
        manager.open();
        ArticleBuffer buffer = new ArticleBuffer();
        final String title = "Hello";
        buffer.title(title.toCharArray(), 0, 5);
        buffer.text("World".toCharArray(), 0, 5);
        manager.save(buffer);
        manager.close();

        ZipFile zip = new ZipFile(file);
        assertEquals("UTF8", zip.getEncoding());
        @SuppressWarnings("rawtypes")
        final Enumeration entries = zip.getEntries();
        assertEquals(true, entries.hasMoreElements());
        final ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
        assertEquals(title, entry.getComment());
        assertEquals(false, entries.hasMoreElements());
    }

    private File file(final String name) {
        return new File(parent, name);
    }
}
