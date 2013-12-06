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

import java.nio.CharBuffer;

import org.itstechupnorth.walrus.text.wiki.TitleTrackingWikiParser;

import junit.framework.TestCase;

public class TitleTrackingWikiParserTest extends TestCase {

    public void testReadTitle() throws Exception {
        TitleTrackingWikiParser parser = new TitleTrackingWikiParser(
                CharBuffer.wrap("==A Title== Note"));
        parser.parse();
        assertTrue("A Title".contentEquals(parser.getLastTitle(1)));
    }

    public void testSecondTitle() throws Exception {
        TitleTrackingWikiParser parser = new TitleTrackingWikiParser(
                CharBuffer.wrap("==A Title== Note==Another== Note"));
        parser.parse();
        assertTrue("Another".contentEquals(parser.getLastTitle(1)));
    }

    public void testLevel2() throws Exception {
        TitleTrackingWikiParser parser = new TitleTrackingWikiParser(
                CharBuffer.wrap("==A Title== Note===Another=== Note"));
        parser.parse();
        assertTrue("A Title".contentEquals(parser.getLastTitle(1)));
        assertTrue("Another".contentEquals(parser.getLastTitle(2)));
    }

    public void testLevel3() throws Exception {
        TitleTrackingWikiParser parser = new TitleTrackingWikiParser(
                CharBuffer
                        .wrap("==A Title== Note===Another=== ====Three====Note"));
        parser.parse();
        assertTrue("A Title".contentEquals(parser.getLastTitle(1)));
        assertTrue("Another".contentEquals(parser.getLastTitle(2)));
        assertTrue("Three".contentEquals(parser.getLastTitle(3)));
    }
}
