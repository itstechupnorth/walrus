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
package org.itstechupnorth.walrus.text.wiki.speech;

import org.itstechupnorth.walrus.base.ArticleBuffer;
import org.itstechupnorth.walrus.base.ArticleProcessor;
import org.itstechupnorth.walrus.text.wiki.micro.BracketFilter;
import org.itstechupnorth.walrus.text.wiki.micro.TitleFilter;
import org.itstechupnorth.walrus.text.wiki.micro.WikiRoundTrip;


public class FilteredLogProcessor extends ArticleProcessor {

    private final String title;
    private final String subTitle;

    public FilteredLogProcessor() {
        this("English", "Pronunciation");
    }

    public FilteredLogProcessor(String title, String subTitle) {
        super();
        this.title = title;
        this.subTitle = subTitle;
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public ArticleBuffer process(ArticleBuffer buffer) throws Exception {
        new WikiRoundTrip(
                BracketFilter.filterCurly(TitleFilter.filterOnSubTitle(
                        TitleFilter.filterOnMainTitle(buffer.toBuffer(), title),
                        subTitle))).writeTo(System.out);
        return buffer;
    }

}
