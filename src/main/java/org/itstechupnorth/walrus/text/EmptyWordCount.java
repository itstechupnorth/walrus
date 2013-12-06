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

public class EmptyWordCount implements WordCount {

    public static final EmptyWordCount INSTANCE = new EmptyWordCount();

    public void add(String word, int count) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
    }

    public Walk descending() {
        return new EmptyWalk();
    }

    public void inc(String word) {
        throw new UnsupportedOperationException();
    }

    public void merge(WordCount wordCount) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return 0;
    }

    public Walk walk() {
        return new EmptyWalk();
    }

    public static final class EmptyWalk implements Walk {

        public int count() {
            return 0;
        }

        public boolean hasNext() {
            return false;
        }

        public String next() {
            return null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}
