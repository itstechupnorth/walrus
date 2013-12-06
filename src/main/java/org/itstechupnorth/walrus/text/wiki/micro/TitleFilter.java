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

import java.nio.CharBuffer;

public class TitleFilter extends AbstractFilter {

    public static TitleFilter filterOnMainTitle(final CharBuffer buffer,
            final String title) {
        return new TitleFilter(new MicroWikiPullParser(buffer), title,
                WikiState.TITLE);
    }

    private static TitleFilter filter(final String content, final String title,
            WikiState state) {
        return new TitleFilter(new MicroWikiPullParser(content), title, state);
    }

    public static TitleFilter filterOnMainTitle(final String content,
            final String title) {
        return filter(content, title, WikiState.TITLE);
    }

    public static TitleFilter filterOnSubTitle(final String content,
            final String title) {
        return filter(content, title, WikiState.SUBTITLE);
    }

    public static TitleFilter filterOnSubTitle(final WikiPullParser parser,
            final String title) {
        return new TitleFilter(parser, title, WikiState.SUBTITLE);
    }

    final String title;
    final WikiState target;

    boolean matchedTitle;

    private TitleFilter(final WikiPullParser parser, final String title,
            final WikiState target) {
        super(parser);
        this.title = title;
        matchedTitle = false;
        this.target = target;
    }

    @Override
    protected WikiState processNext(final WikiTracker tracker,
            final WikiState next) {
        if (next == target) {
            if (tracker.isInTitle()) {
                return next();
            } else {
                matchedTitle = title.contentEquals(text());
                return next();
            }
        }

        if (matchedTitle) {
            return next;
        } else {
            return next();
        }
    }

    @Override
    protected void doClear() {
        matchedTitle = false;
    }

    @Override
    public String toString() {
        return "TitleFilter [" + super.toString() + ", target=" + target
                + ", title=" + title + "]";
    }
}
