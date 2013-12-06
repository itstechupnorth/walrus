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
package org.itstechupnorth.walrus;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.itstechupnorth.walrus.app.Walrus;


public class SubjectOption extends WalrusOption {

    private enum Opt {
        ZIP(Walrus.Subject.ARCHIVE, "zip", "operate on zip dump", 'w'), DIRECTORY(
                Walrus.Subject.DIRECTORY, "dir", "operate on directory", 'd'), WIKI(
                Walrus.Subject.WIKI, "wiki", "operate on wiki dump", 'z');

        private final Walrus.Subject subject;
        private final String longName;
        private final String description;
        private final char abbreviation;

        private Opt(Walrus.Subject subject, String longName,
                String description, char abbreviation) {
            this.subject = subject;
            this.longName = longName;
            this.description = description;
            this.abbreviation = abbreviation;
        }

        @SuppressWarnings("static-access")
        public Option option() {
            return OptionBuilder.withArgName(longName).withLongOpt(longName)
                    .withDescription(description).create(abbreviation);
        }

        public void configure(CommandLine line, Walrus walrus) {
            if (line.hasOption(longName)) {
                walrus.setSubject(subject);
            }
        }
    }

    @Override
    public void addTo(Options options) {
        final OptionGroup group = new OptionGroup();
        for (Opt opt : Opt.values()) {
            group.addOption(opt.option());
        }
        options.addOptionGroup(group);
    }

    @Override
    public void configure(CommandLine line, Walrus walrus) {
        for (Opt opt : Opt.values()) {
            opt.configure(line, walrus);
        }
    }
}
