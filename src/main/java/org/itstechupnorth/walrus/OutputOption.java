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
import org.apache.commons.cli.Options;
import org.itstechupnorth.walrus.app.Walrus;


public class OutputOption extends WalrusOption {

    private static final String NAME = "output";

    @Override
    public void addTo(Options options) {
        options.addOption("o", NAME, true, "output file path");
    }

    @Override
    public void configure(CommandLine line, Walrus walrus) {
        if (line.hasOption(NAME)) {
            final String optionValue = line.getOptionValue(NAME);
            walrus.setOutputFile(optionValue);
        }
    }

}
