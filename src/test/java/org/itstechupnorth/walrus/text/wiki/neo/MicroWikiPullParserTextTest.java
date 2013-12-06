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


public class MicroWikiPullParserTextTest extends TestCase {

    public void testEnd() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("Roger");
        assertEquals(WikiState.END, parser.next());
        assertEquals("Roger", parser.text().toString());
    }

    public void testOpenBracket() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("Tree{{Fish");
        assertEquals(WikiState.OPEN_CURLY, parser.next());
        assertEquals("Tree", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("Fish", parser.text().toString());
    }

    public void testCloseBracket() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("Wiggle}}Tom");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals("Wiggle", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("Tom", parser.text().toString());
    }

    public void testCloseResetBracket() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("alpha}}beta{{gamma");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals("alpha", parser.text().toString());
        assertEquals(WikiState.OPEN_CURLY, parser.next());
        assertEquals("beta", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("gamma", parser.text().toString());
    }

    public void testOpenSquare() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("alpha[[beta");
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals("alpha", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("beta", parser.text().toString());
    }

    public void testCloseSquare() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("sid]]nancy");
        assertEquals(WikiState.CLOSE_SQUARE, parser.next());
        assertEquals("sid", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("nancy", parser.text().toString());
    }

    public void testCloseResetSquare() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("alpha]]beta[[gamma");
        assertEquals(WikiState.CLOSE_SQUARE, parser.next());
        assertEquals("alpha", parser.text().toString());
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals("beta", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("gamma", parser.text().toString());
    }

    public void testHash() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("once]]more#unto");
        assertEquals(WikiState.CLOSE_SQUARE, parser.next());
        assertEquals("once", parser.text().toString());
        assertEquals(WikiState.HASH, parser.next());
        assertEquals("more", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("unto", parser.text().toString());
    }

    public void testStar() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("fee]]fie*foo");
        assertEquals(WikiState.CLOSE_SQUARE, parser.next());
        assertEquals("fee", parser.text().toString());
        assertEquals(WikiState.STAR, parser.next());
        assertEquals("fie", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("foo", parser.text().toString());
    }

    public void testPipe() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("one{{two|tea");
        assertEquals(WikiState.OPEN_CURLY, parser.next());
        assertEquals("one", parser.text().toString());
        assertEquals(WikiState.PIPE, parser.next());
        assertEquals("two", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("tea", parser.text().toString());
    }

    public void testReset() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("space{ { [[end");
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals("space{ { ", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("end", parser.text().toString());
    }

    public void testLF() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + CharUtils.LF);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.END, parser.next());
        assertEquals("" + CharUtils.LF, parser.text().toString());
    }

    public void testCR() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + CharUtils.CR);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.END, parser.next());
        assertEquals("" + CharUtils.CR, parser.text().toString());
    }

    public void testCRLF() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + CharUtils.CR
                + CharUtils.LF);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.END, parser.next());
        assertEquals("" + CharUtils.CR + CharUtils.LF, parser.text().toString());
    }

    public void testCRCR() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + CharUtils.CR
                + CharUtils.CR);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.EMPTY_LINE, parser.next());
        assertEquals("", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
    }

    public void testCRCRStuff() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + "stuff"
                + CharUtils.CR + CharUtils.CR);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.EMPTY_LINE, parser.next());
        assertEquals("stuff", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
    }

    public void testCRLFCRLF() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + CharUtils.CR
                + CharUtils.LF + CharUtils.CR + CharUtils.LF);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.EMPTY_LINE, parser.next());
        assertEquals("", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
    }

    public void testCRLFCRLFStuff() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + "More"
                + CharUtils.CR + CharUtils.LF + CharUtils.CR + CharUtils.LF);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.EMPTY_LINE, parser.next());
        assertEquals("More", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
    }

    public void testLFLF() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + CharUtils.LF
                + CharUtils.LF);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.EMPTY_LINE, parser.next());
        assertEquals("", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
    }

    public void testLFLFStuff() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("[[" + "And"
                + CharUtils.LF + CharUtils.LF);
        assertEquals(WikiState.OPEN_SQUARE, parser.next());
        assertEquals(WikiState.EMPTY_LINE, parser.next());
        assertEquals("And", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
    }

    public void testTitle() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("one}}two==three");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals("one", parser.text().toString());
        assertEquals(WikiState.TITLE, parser.next());
        assertEquals("two", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("three", parser.text().toString());
    }

    public void testSubTitle() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("alpha}}beta===gamma");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals("alpha", parser.text().toString());
        assertEquals(WikiState.SUBTITLE, parser.next());
        assertEquals("beta", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("gamma", parser.text().toString());
    }

    public void testSubSubTitle() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("x}}y====z");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals("x", parser.text().toString());
        assertEquals(WikiState.SUBSUBTITLE, parser.next());
        assertEquals("y", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("z", parser.text().toString());
    }

    public void testSubSubSubTitle() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("foo}}bar=====so");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals("foo", parser.text().toString());
        assertEquals(WikiState.SUB3TITLE, parser.next());
        assertEquals("bar", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("so", parser.text().toString());
    }

    public void testSubSubSubSubTitle() throws Exception {
        WikiPullParser parser = new MicroWikiPullParser("alt}}ctrl======gr");
        assertEquals(WikiState.CLOSE_CURLY, parser.next());
        assertEquals("alt", parser.text().toString());
        assertEquals(WikiState.SUB4TITLE, parser.next());
        assertEquals("ctrl", parser.text().toString());
        assertEquals(WikiState.END, parser.next());
        assertEquals("gr", parser.text().toString());
    }
}
