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
package org.itstechupnorth.walrus.text.concise;

public class WordCount {

    private int count = 0;
    private WordCount[] children = null;

    public void inc() {
        count++;
    }

    public int count() {
        return count;
    }

    public WordCount next(char character) {
        if (children == null) {
            children = new WordCount[26];
        }
        final int index = index(character);
        if (index < 0) {
            return null;
        }
        if (children[index] == null) {
            children[index] = new WordCount();
        }
        return children[index];
    }

    public int index(char character) {
        switch (character) {
            case '\'':
                return 0;
            case 'a':
            case 'A':
                return 1;
            case 'b':
            case 'B':
                return 2;
            case 'c':
            case 'C':
                return 3;
            case 'd':
            case 'D':
                return 4;
            case 'e':
            case 'E':
                return 5;
            case 'f':
            case 'F':
                return 6;
            case 'g':
            case 'G':
                return 7;
            case 'h':
            case 'H':
                return 8;
            case 'i':
            case 'I':
                return 9;
            case 'j':
            case 'J':
                return 10;
            case 'k':
            case 'K':
                return 11;
            case 'l':
            case 'L':
                return 12;
            case 'm':
            case 'M':
                return 13;
            case 'n':
            case 'N':
                return 14;
            case 'o':
            case 'O':
                return 15;
            case 'p':
            case 'P':
                return 16;
            case 'q':
            case 'Q':
                return 17;
            case 'r':
            case 'R':
                return 18;
            case 's':
            case 'S':
                return 19;
            case 't':
            case 'T':
                return 20;
            case 'u':
            case 'U':
                return 21;
            case 'v':
            case 'V':
                return 22;
            case 'w':
            case 'W':
                return 23;
            case 'x':
            case 'X':
                return 24;
            case 'y':
            case 'Y':
                return 25;
            case 'z':
            case 'Z':
                return 26;
            default:
                return -1;
        }
    }
}
