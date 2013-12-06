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
import org.itstechupnorth.walrus.app.Walrus.Act;


public class ActOption extends WalrusOption {

    private enum Opt {
        ARCHIVE(Walrus.Act.ARCHIVE, "archive", "archive", 'r'), COUNT(
                Walrus.Act.COUNT, "count", "count", 'c'), LOG(Walrus.Act.LOG,
                "log", "log", 'l');

        private final Walrus.Act act;
        private final String longName;
        private final String description;
        private final char abbreviation;

        private Opt(Act act, String longName, String description,
                char abbreviation) {
            this.act = act;
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
                walrus.setAct(act);
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
