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
package org.itstechupnorth.walrus.text.wiki.micro;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.FileUtils;

public class WikiRoundTrip {

    private static final String FILE = "target/article-59501f47ffd57218f69e9e5abf6b33cc.txt";

    public static final void main(String[] args) throws Exception {
        new WikiRoundTrip(BracketFilter.filterCurly(TitleFilter
                .filterOnSubTitle(TitleFilter.filterOnMainTitle(
                        FileUtils.readFileToString(new File(FILE)),
                        "Serbo-Croatian"), "Pronunciation")))
                .writeTo(System.out);
    }

    private final WikiPullParser parser;

    public WikiRoundTrip(WikiPullParser parser) {
        super();
        this.parser = parser;
    }

    public void writeTo(OutputStream out) throws IOException {
        writeTo(new OutputStreamWriter(out));
    }

    public void writeTo(Writer writer) throws IOException {
        while (writeNext(writer) != WikiState.END)
            ;
        writer.flush();
    }

    public WikiState writeNext(Writer writer) throws IOException {
        final WikiState next = parser.next();
        CharSequence text = parser.text();
        writer.write(text.toString());
        writer.write(next.token());
        return next;
    }
}
