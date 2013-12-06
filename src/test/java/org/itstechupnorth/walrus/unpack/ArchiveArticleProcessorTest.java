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
package org.itstechupnorth.walrus.unpack;

import java.io.File;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.itstechupnorth.walrus.base.ArticleBuffer;
import org.itstechupnorth.walrus.zip.ArchiveArticleProcessor;


public class ArchiveArticleProcessorTest extends TestCase {

    ArchiveArticleProcessor processor;
    private File directory;

    @Override
    protected void setUp() throws Exception {
        directory = new File("target/tests/processors");
        directory.mkdirs();
        FileUtils.cleanDirectory(directory);
        processor = new ArchiveArticleProcessor(directory, 4);
    }

    public void testProcessOne() throws Exception {
        ArticleBuffer buffer = new ArticleBuffer();
        final String title = "Hello";
        buffer.title(title.toCharArray(), 0, 5);
        buffer.text("World".toCharArray(), 0, 5);
        processor.process(buffer);
        processor.close();
        assertEquals(1, directory.list().length);
    }

    public void testProcess36() throws Exception {
        ArticleBuffer buffer = new ArticleBuffer();

        for (int i = 0; i < 128; i++) {
            buffer.reset();
            final String title = "Hello-" + i;
            buffer.title(title.toCharArray(), 0, 5);
            buffer.text("World".toCharArray(), 0, 5);
            processor.process(buffer);
        }
        processor.close();
        assertEquals(4, directory.list().length);
    }

}
