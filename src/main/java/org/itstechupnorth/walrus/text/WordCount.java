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

import java.util.Iterator;

public interface WordCount {

    public interface Walk extends Iterator<String> {
        public int count();
    }

    public Walk walk();

    public Walk descending();

    public void inc(String word) throws UnsupportedOperationException;

    public void add(String word, int count)
            throws UnsupportedOperationException;

    public void merge(WordCount wordCount) throws UnsupportedOperationException;

    public void clear();

    public int size();
}
