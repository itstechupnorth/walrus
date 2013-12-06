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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class MapWordCount implements WordCount {

    private final static class Counter implements Comparable<Counter> {
        private final String string;
        private int count;

        public Counter(String word) {
            this(word, 1);
        }

        public Counter(String word, int count) {
            super();
            this.string = word.intern();
            this.count = count;
        }

        public void inc() {
            count++;
        }

        public String getString() {
            return string;
        }

        @Override
        public int hashCode() {
            return string.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Counter other = (Counter) obj;
            return string == other.string;
        }

        public int compareTo(Counter c) {
            final int diff = c.count - count;
            if (diff == 0) {
                return c.string.compareTo(string);
            }
            return diff;
        }

        @Override
        public String toString() {
            return string + " " + count;
        }

        public void add(int count) {
            this.count = this.count + count;
        }
    }

    private final Map<String, Counter> count = new HashMap<String, Counter>();

    public void add(String word, int count) {
        if (this.count.containsKey(word)) {
            this.count.get(word).add(count);
        } else {
            this.count.put(word, new Counter(word, count));
        }
    }

    public Walk descending() {
        return new CollectionWalk(
                new TreeSet<Counter>(count.values()).iterator());
    }

    public void inc(String word) {
        if (count.containsKey(word)) {
            count.get(word).inc();
        } else {
            count.put(word, new Counter(word));
        }
    }

    public void merge(WordCount wordCount) {
        final Walk walk = wordCount.walk();
        while (walk.hasNext()) {
            final String next = walk.next();
            final int count = walk.count();
            add(next, count);
        }
    }

    public Walk walk() {
        return new CollectionWalk(count.values().iterator());
    }

    private static final class CollectionWalk implements Walk {

        private final Iterator<Counter> iterator;

        private Counter current;

        public CollectionWalk(Iterator<Counter> iterator) {
            super();
            this.iterator = iterator;
        }

        public int count() {
            if (current == null) {
                return 0;
            }
            return current.count;
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public String next() {
            current = iterator.next();
            return current.getString();
        }

        public void remove() {
            throw new UnsupportedOperationException("Read only");
        }
    }

    public void clear() {
        count.clear();
    }

    public int size() {
        return count.size();
    }
}
