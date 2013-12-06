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
package org.itstechupnorth.walrus.app;

import java.io.File;
import java.util.Date;

import org.itstechupnorth.walrus.base.ArticleProcessor;
import org.itstechupnorth.walrus.base.Constants;
import org.itstechupnorth.walrus.engine.ArticleProcessingEngine;
import org.itstechupnorth.walrus.unpack.xml.QueuingArticleLoader;


public abstract class WikiProcessor {

    private final String pathname;

    private int threads = Constants.DEFAULT_NUMBER_OF_THREADS;
    private int buffers = Constants.DEFAULT_NUMBER_OF_BUFFERS;

    public WikiProcessor(String pathname) {
        super();
        this.pathname = pathname;
    }

    public int getThreads() {
        return threads;
    }

    public WikiProcessor setThreads(int threads) {
        this.threads = threads;
        return this;
    }

    public int getBuffers() {
        return buffers;
    }

    public WikiProcessor setBuffers(int buffers) {
        this.buffers = buffers;
        return this;
    }

    public void perform() throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("Started " + now());
        final ArticleProcessor processor = createProcessor();
        ArticleProcessingEngine engine = new ArticleProcessingEngine(processor,
                buffers, threads);
        new Thread(engine).start();
        new QueuingArticleLoader(new File(pathname), engine.getOut(),
                engine.getIn()).load();
        long end = System.currentTimeMillis();
        System.out.println("Ended " + now());
        System.out.println("Duration " + (end - start) / 1000f + " (secs)");
        engine.finish();
        engine.waitTillfinished();
        processor.close();
    }

    @SuppressWarnings("deprecation")
    private String now() {
        return new Date().toGMTString();
    }

    protected abstract ArticleProcessor createProcessor();
}