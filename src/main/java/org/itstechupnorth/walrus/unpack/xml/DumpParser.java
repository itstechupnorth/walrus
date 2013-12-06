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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class DumpParser extends DumpLoader {

    public enum Section {
        TITLE, TEXT, OTHER
    };

    private Section section = Section.OTHER;

    protected abstract void title(char[] ch, int start, int length);

    protected abstract void text(char[] ch, int start, int length);

    protected abstract void startSection(Section section);

    protected abstract void endText();

    public DumpParser(File target) {
        super(target);
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        final Section oldSection = this.section;
        if ("title".equals(qName)) {
            section = Section.TITLE;
        } else if ("text".equals(qName)) {
            section = Section.TEXT;
        } else {
            section = Section.OTHER;
        }
        if (oldSection != section) {
            startSection(section);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
        if ("text".equals(qName)) {
            endText();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        switch (section) {
            case TEXT:
                text(ch, start, length);
                break;
            case TITLE:
                title(ch, start, length);
                break;
            default:
                break;
        }
    }

}