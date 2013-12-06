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

public class WikiTracker {

    private boolean inTitle;
    private boolean inCurly;
    private boolean inSquare;
    private int parameterNumber;
    private boolean inList;

    public WikiTracker() {
        reset();
    }

    public void reset() {
        inTitle = false;
        inCurly = false;
        inSquare = false;
    }

    public void next(final WikiState state) {
        switch (state) {
            case CLOSE_CURLY:
                inCurly = false;
                parameterNumber = 0;
                break;
            case OPEN_CURLY:
                inCurly = true;
                break;
            case TITLE:
            case SUBTITLE:
            case SUBSUBTITLE:
            case SUB3TITLE:
            case SUB4TITLE:
                inTitle = !inTitle;
                break;
            case OPEN_SQUARE:
                inSquare = true;
                break;
            case CLOSE_SQUARE:
                parameterNumber = 0;
                inSquare = false;
                break;
            case PIPE:
                parameterNumber++;
                break;
            case HASH:
            case STAR:
                inList = true;
                break;
            case EMPTY_LINE:
                inList = false;
                break;
            default:
                break;
        }
    }

    public boolean isInTitle() {
        return inTitle;
    }

    public boolean isInCurly() {
        return inCurly;
    }

    public boolean isInSquare() {
        return inSquare;
    }

    public int getParameterNumber() {
        return parameterNumber;
    }

    public boolean isInList() {
        return inList;
    }

    @Override
    public String toString() {
        return "WikiTracker [inCurly=" + inCurly + ", inList=" + inList
                + ", inSquare=" + inSquare + ", inTitle=" + inTitle
                + ", parameterNumber=" + parameterNumber + "]";
    }

}
