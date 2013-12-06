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

public class DefaultDumpHandler extends DumpParser {

    public DefaultDumpHandler(File target) {
        super(target);
    }

    @Override
    protected void text(char[] ch, int start, int length) {
    }

    @Override
    protected void title(char[] ch, int start, int length) {
    }

    @Override
    protected void startSection(Section section) {
    }

    @Override
    protected void endText() {
    }

}
