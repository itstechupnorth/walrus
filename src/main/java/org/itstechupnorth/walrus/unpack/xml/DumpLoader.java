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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

abstract class DumpLoader extends DefaultHandler {

    private final File target;

    private BZip2CompressorInputStream in;

    public DumpLoader(File target) {
        super();
        this.target = target;
    }

    private void open() throws IOException {
        if (!target.exists()) {
            throw new FileNotFoundException("File not found " + target);
        }
        in = new BZip2CompressorInputStream(new FileInputStream(target));
    }

    private void parse() throws ParserConfigurationException, SAXException,
            IOException {
        SAXParserFactory.newInstance().newSAXParser().parse(in, this);
    }

    private void close() throws IOException {
        if (in != null) {
            in.close();
        }
    }

    public void load() throws IOException, ParserConfigurationException,
            SAXException {
        try {
            open();
            parse();
        } finally {
            close();
        }
    }

}
