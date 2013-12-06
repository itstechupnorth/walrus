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
package org.itstechupnorth.walrus.text.wiki;

import java.nio.CharBuffer;

public class TitleTrackingWikiParser extends BasicWikiParser {

    private TitleTracker[] trackers = new TitleTracker[10];

    public TitleTrackingWikiParser(CharBuffer buffer) {
        super(buffer);
    }

    @Override
    protected void title(int numberOfDuplicates) {
        super.title(numberOfDuplicates);
        final int at = at();
        lazyCreate(numberOfDuplicates);
        final TitleTracker titleTracker = trackers[numberOfDuplicates];
        if (titleTracker.mark(at)) {
            final CharSequence title = get(titleTracker.getStart(),
                    titleTracker.getEnd());
            // System.out.println(title);
            titleTracker.setTitle(title);
        }
    }

    private void lazyCreate(int level) {
        if (trackers[level] == null) {
            trackers[level] = new TitleTracker(level);
        }
    }

    @Override
    protected void reset() {
        super.reset();
        for (TitleTracker tracker : trackers) {
            if (tracker != null) {
                tracker.reset();
            }
        }
    }

    public CharSequence getLastTitle(int level) {
        lazyCreate(level);
        return trackers[level].getTitle();
    }
}
