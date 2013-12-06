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
import org.apache.commons.cli.ParseException;
import org.itstechupnorth.walrus.app.Walrus;


public abstract class WalrusOption {

    public WalrusOption() {
        super();
    }

    public abstract void configure(final CommandLine line, final Walrus walrus)
            throws ParseException;

    public abstract void addTo(final Options options);

    protected int parseInt(CommandLine line, final String name)
            throws ParseException {
        final String value = line.getOptionValue(name);
        // System.out.println(value);
        return Integer.valueOf(value);
    }
}