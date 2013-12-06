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

public class TitleTracker {

    private static final int UNSET = -1;

    private final int levels;
    private int start;
    private int end;
    private boolean isInTitle;
    private CharSequence title;

    public TitleTracker(int levels) {
        this.levels = levels;
    }

    public void reset() {
        start = UNSET;
        end = UNSET;
        isInTitle = false;
        title = null;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public boolean titleIs(String name) {
        return name.contentEquals(title);
    }

    public boolean mark(int at) {
        if (isInTitle) {
            end = at - levels - 2;
            isInTitle = false;
        } else {
            start = at - 1;
            end = UNSET;
            isInTitle = true;
        }
        return !isInTitle;
    }
}
