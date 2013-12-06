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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.itstechupnorth.walrus.app.Walrus;


public class App {
    public static void main(String[] args) throws Exception {
        new App().configure(args).runWalrus();
    }

    private final Collection<WalrusOption> options;

    public App() {
        options = new ArrayList<WalrusOption>();
        options.add(new PathOption());
        options.add(new ActOption());
        options.add(new SubjectOption());
        options.add(new ThreadsOption());
        options.add(new BuffersOption());
        options.add(new OutputOption());
    }

    public CommandLineParser parser() {
        return new GnuParser();
    }

    public CommandLine parse(String[] args) throws ParseException {
        return parser().parse(options(), args);
    }

    private Options options() {
        final Options results = new Options();
        for (WalrusOption option : options) {
            option.addTo(results);
        }
        return results;
    }

    public Walrus configure(final CommandLine line) throws ParseException {
        Walrus walrus = new Walrus();
        for (WalrusOption option : options) {
            option.configure(line, walrus);
        }
        return walrus;
    }

    public Walrus configure(final String[] args) throws ParseException {
        return configure(parse(args));
    }
}
