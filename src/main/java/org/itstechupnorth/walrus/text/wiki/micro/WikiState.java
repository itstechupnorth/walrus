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

import org.apache.commons.lang.CharUtils;

public enum WikiState {
    OPEN_CURLY("{{", true, true), END(""), CLOSE_CURLY("}}", true, false), CLOSE_SQUARE(
            "]]", false, false), OPEN_SQUARE("[[", false, true), HASH("#"), STAR(
            "*"), PIPE("|"), EMPTY_LINE("" + CharUtils.CR + CharUtils.LF
            + CharUtils.CR + CharUtils.LF), TITLE("=="), SUBTITLE("==="), SUBSUBTITLE(
            "===="), SUB3TITLE("====="), SUB4TITLE("======");

    private final String token;
    private final boolean curly;
    private final boolean square;
    private final boolean open;
    private final boolean close;

    private WikiState(String token) {
        this(token, false, false, false, false);
    }

    private WikiState(String token, boolean curly, boolean open) {
        this(token, curly, !curly, open, !open);
    }

    private WikiState(String token, boolean curly, boolean square,
            boolean open, boolean close) {
        this.token = token;
        this.curly = curly;
        this.square = square;
        this.open = open;
        this.close = close;
    }

    public String token() {
        return token;
    }

    public boolean isCurly() {
        return curly;
    }

    public boolean isSquare() {
        return square;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isClose() {
        return close;
    }
}