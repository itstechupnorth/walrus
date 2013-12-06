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

import java.util.ArrayList;
import java.util.List;

import org.itstechupnorth.walrus.base.CharProcessor;


public class SimpleWordParser implements CharProcessor {

    private StringBuilder builder;
    private final List<String> words = new ArrayList<String>(9128);

    public SimpleWordParser() {
    }

    public CharProcessor process(char next) {
        if (Chars.isWhitespace(next) || Chars.isPunctuation(next)) {
            if (builder != null) {
                word();
                builder = null;
            }
        } else {
            if (builder == null) {
                builder = new StringBuilder();
            }
            builder.append(next);
        }
        return this;
    }

    protected void word() {
        if (builder != null) {
            final String word = builder.toString().intern();
            words.add(word);
        }
    }

    public void eof() {
        word();
    }

    public List<String> getWords() {
        return words;
    }

    public void clear() {
        words.clear();
    }
}
