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
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.itstechupnorth.walrus.app.Walrus;


public class ThreadsOption extends WalrusOption {

    private static final String NAME = "threads";

    @SuppressWarnings("static-access")
    @Override
    public void addTo(Options options) {
        options.addOption(OptionBuilder.withArgName(NAME).hasArg()
                .withDescription("number of threads").withType(Integer.class)
                .withLongOpt("threads").create('t'));
    }

    @Override
    public void configure(CommandLine line, Walrus walrus)
            throws ParseException {
        if (line.hasOption(NAME)) {
            walrus.setThreads(parseInt(line, NAME));
        }
    }

}
