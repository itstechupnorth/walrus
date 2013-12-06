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

public class BracketFilter extends AbstractFilter {

    public static BracketFilter filterCurly(WikiPullParser parser) {
        return new BracketFilter(parser, true);
    }

    public static BracketFilter filterOnSquare(WikiPullParser parser) {
        return new BracketFilter(parser, false);
    }

    private final boolean curly;

    private BracketFilter(WikiPullParser parser, final boolean curly) {
        super(parser);
        this.curly = curly;
    }

    @Override
    protected void doClear() {
    }

    @Override
    protected WikiState processNext(WikiTracker tracker, WikiState next) {
        if (curly && (tracker.isInCurly() || next == WikiState.CLOSE_CURLY)
                || !curly
                && (tracker.isInSquare() || next == WikiState.CLOSE_SQUARE)) {
            return next;
        } else {
            return next();
        }
    }

}
