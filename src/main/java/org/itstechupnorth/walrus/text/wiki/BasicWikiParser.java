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

public class BasicWikiParser {

    private final CharBuffer buffer;

    private int numberOfDuplicates;
    private char last;

    public BasicWikiParser(final CharBuffer buffer) {
        this.buffer = buffer;
    }

    public void parse() {
        reset();
        buffer.rewind();
        numberOfDuplicates = 0;
        last = 0;
        while (buffer.hasRemaining()) {
            next();
        }
    }

    public CharSequence get(final int start, final int end) {
        final int oldPosition = buffer.position();
        buffer.position(0);
        final CharSequence result = buffer.subSequence(start, end);
        buffer.position(oldPosition);
        return result;
    }

    public int at() {
        return buffer.position();
    }

    protected void reset() {
    }

    private void next() {
        final char next = buffer.get();
        if (next == last) {
            numberOfDuplicates++;
        } else {
            if (numberOfDuplicates > 0 && last == '=') {
                title(numberOfDuplicates);
            } else if (numberOfDuplicates == 1) {
                switch (last) {
                    case '[':
                        openSquare();
                        break;
                    case ']':
                        closeSquare();
                        break;
                    case '{':
                        openCurly();
                        break;
                    case '}':
                        closeSquare();
                        break;
                }
            } else if (numberOfDuplicates == 2) {
                switch (last) {
                    case '\'':
                        quote();
                        break;
                }
            }
            numberOfDuplicates = 0;
            last = next;
        }
    }

    protected void quote() {
    }

    protected void openCurly() {
    }

    protected void closeSquare() {
    }

    protected void openSquare() {
    }

    protected void title(final int numberOfDuplicates) {
    }
}
