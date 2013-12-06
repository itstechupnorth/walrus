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
package org.itstechupnorth.walrus.unpack.xml;

import java.io.File;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;

public class TextCollector extends TitlePrinter {

    private static final int K = 1024;
    private static final int M = 1024 * K;
    private static final int BUFFER = 8 * M;

    public TextCollector(File target) {
        super(target);
    }

    private CharBuffer buffer = CharBuffer.allocate(BUFFER);

    @Override
    protected void startSection(Section section) {
        super.startSection(section);
        if (section == Section.TEXT) {
            buffer.clear();
        }
    }

    @Override
    protected void text(char[] ch, int start, int length) {
        super.text(ch, start, length);
        try {
            buffer.put(ch, start, length);
            System.out.println(buffer.subSequence(0,
                    Math.min(10, buffer.length())));
        } catch (BufferOverflowException e) {
            throw new RuntimeException("Overflow");
        }
    }
}
