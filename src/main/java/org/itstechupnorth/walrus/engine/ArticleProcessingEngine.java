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
package org.itstechupnorth.walrus.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.itstechupnorth.walrus.base.ArticleBuffer;
import org.itstechupnorth.walrus.base.ArticleProcessor;
import org.itstechupnorth.walrus.base.Constants;


public class ArticleProcessingEngine implements Runnable {

    /** MUST be accessed only by the contained thread */
    private final List<Future<ArticleBuffer>> inFlight;
    private final BlockingQueue<ArticleBuffer> in;
    private final BlockingQueue<ArticleBuffer> out;
    private final ExecutorService executor;
    private final ArticleProcessor processor;

    private final CountDownLatch doneLatch = new CountDownLatch(1);

    private boolean stop = false;
    private boolean finish = false;

    public ArticleProcessingEngine(final ArticleProcessor processor) {
        this(Constants.DEFAULT_NUMBER_OF_BUFFERS,
                Constants.DEFAULT_NUMBER_OF_THREADS, Executors
                        .defaultThreadFactory(), processor);
    }

    public ArticleProcessingEngine(final ArticleProcessor processor,
            int buffers, final int numberOfThreads) {
        this(buffers, numberOfThreads, Executors.defaultThreadFactory(),
                processor);
    }

    public ArticleProcessingEngine(int buffers, final int numberOfThreads,
            final ThreadFactory threadFactory, final ArticleProcessor processor) {
        this(buffers, Executors.newFixedThreadPool(numberOfThreads,
                threadFactory), processor);
    }

    public ArticleProcessingEngine(int buffers, final ExecutorService executor,
            final ArticleProcessor processor) {
        this.executor = executor;
        this.in = new ArrayBlockingQueue<ArticleBuffer>(buffers);
        this.out = new ArrayBlockingQueue<ArticleBuffer>(buffers);
        for (int i = 0; i < buffers; i++) {
            out.add(new ArticleBuffer());
        }
        this.inFlight = new ArrayList<Future<ArticleBuffer>>(buffers);
        this.processor = processor;
    }

    public BlockingQueue<ArticleBuffer> getIn() {
        return in;
    }

    public BlockingQueue<ArticleBuffer> getOut() {
        return out;
    }

    public void run() {
        // System.out.println(this);
        while (!stop) {
            try {
                final ArticleBuffer incoming = in
                        .poll(1, TimeUnit.MILLISECONDS);
                process(incoming);
                forwardCompleted();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (finish) {
            try {
                System.out.println("Tidying up...");
                while (!in.isEmpty()) {
                    try {
                        process(in.poll());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                executor.shutdown();
                while (!executor.isTerminated())
                    ;
                try {
                    forwardCompleted();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } finally {
                System.out.println("Done.");
                doneLatch.countDown();
            }
        }
    }

    private void forwardCompleted() throws InterruptedException,
            ExecutionException {
        for (final Iterator<Future<ArticleBuffer>> it = inFlight.iterator(); it
                .hasNext();) {
            final Future<ArticleBuffer> future = it.next();
            if (future.isDone()) {
                final ArticleBuffer buffer = future.get();
                buffer.reset();
                out.put(buffer);
                it.remove();
                // System.out.println(buffer + " END PROCESSING");
            }
        }
    }

    private void process(final ArticleBuffer incoming)
            throws InterruptedException {
        if (incoming != null) {
            // System.out.println(incoming + " START PROCESSING");
            inFlight.add(executor.submit(newCall(incoming)));
        }
    }

    protected Callable<ArticleBuffer> newCall(final ArticleBuffer incoming) {
        return new ProcessingTask(incoming, processor);
    }

    public void stop() {
        this.stop = true;
    }

    public void finish() {
        stop = true;
        finish = true;
    }

    public void waitTillfinished() throws InterruptedException {
        doneLatch.await();
    }

    @Override
    public String toString() {
        return "ArticleProcessingEngine [doneLatch=" + doneLatch
                + ", executor=" + executor + ", finish=" + finish + ", in="
                + in + ", inFlight=" + inFlight + ", out=" + out
                + ", processor=" + processor + ", stop=" + stop + "]";
    }
}
