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
package org.itstechupnorth.walrus.text.wiki.neo;

import junit.framework.TestCase;

import org.apache.commons.lang.CharUtils;
import org.itstechupnorth.walrus.text.wiki.micro.MicroWikiPullParser;
import org.itstechupnorth.walrus.text.wiki.micro.WikiPullParser;
import org.itstechupnorth.walrus.text.wiki.micro.WikiState;


public class MicroWikiPullParserStateTest extends TestCase {

    public void testEnd() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("");
        assertEquals(WikiState.END, parser.next());
    }

    public void testOpenBracket() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("{{");
        assertEquals(WikiState.OPEN_CURLY, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testCloseBracket() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("}}");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testCloseResetBracket() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("}}{{");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals(WikiState.OPEN_CURLY, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testOpenSquare() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[");
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testCloseSquare() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("]]");
        assertEquals(WikiState.CLOSE_SQUARE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testCloseResetSquare() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("]][[");
        assertEquals(WikiState.CLOSE_SQUARE, parser.next());
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testHash() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("]]#");
        assertEquals(WikiState.CLOSE_SQUARE, parser.next());
        assertEquals(WikiState.HASH, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testStar() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("]]*");
        assertEquals(WikiState.CLOSE_SQUARE, parser.next());
        assertEquals(WikiState.STAR, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testPipe() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("{{|");
        assertEquals(WikiState.OPEN_CURLY, parser.next());
        assertEquals(WikiState.PIPE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testReset() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("{ { [[");
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testLF() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + CharUtils.LF);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testCR() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + CharUtils.CR);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testCRLF() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + CharUtils.CR
                + CharUtils.LF);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testCRCR() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + CharUtils.CR
                + CharUtils.CR);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.EMPTY_LINE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testCRLFCRLF() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + CharUtils.CR
                + CharUtils.LF + CharUtils.CR + CharUtils.LF);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.EMPTY_LINE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testLFLF() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + CharUtils.LF
                + CharUtils.LF);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.EMPTY_LINE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testTitle() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("}}==");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals(WikiState.TITLE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testSubTitle() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("}}===");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals(WikiState.SUBTITLE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testSubSubTitle() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("}}====");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals(WikiState.SUBSUBTITLE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testSubSubSubTitle() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("}}=====");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals(WikiState.SUB3TITLE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }

    public void testSubSubSubSubTitle() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("}}======");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals(WikiState.SUB4TITLE, parser.next());
        assertEquals(WikiState.END, parser.next());
    }
}
