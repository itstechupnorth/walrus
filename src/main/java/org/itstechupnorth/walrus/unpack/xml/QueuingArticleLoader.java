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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.itstechupnorth.walrus.base.ArticleBuffer;


public class QueuingArticleLoader extends ArticleLoader {

    private static final int BUFFER_CAPACITY = 10;
    private final BlockingQueue<ArticleBuffer> in;
    private final BlockingQueue<ArticleBuffer> out;

    public QueuingArticleLoader(File target) {
        this(target, new ArrayBlockingQueue<ArticleBuffer>(BUFFER_CAPACITY));
    }

    public QueuingArticleLoader(File target, BlockingQueue<ArticleBuffer> in,
            BlockingQueue<ArticleBuffer> out) {
        super(target);
        this.in = in;
        this.out = out;
    }

    public QueuingArticleLoader(File target, BlockingQueue<ArticleBuffer> queue) {
        this(target, queue, queue);
        for (int i = 1; i < BUFFER_CAPACITY; i++) {
            in.add(new ArticleBuffer());
        }
    }

    @Override
    protected void endText() {
        final ArticleBuffer buffer = retireBuffer();
        try {
            out.put(buffer);
            // System.out.println(buffer + " END FILL");
        } catch (InterruptedException e) {
            e.printStackTrace();
            endText();
        }
    }

    @Override
    protected ArticleBuffer next() {
        try {
            final ArticleBuffer result = in.take();
            // System.out.println(result + " START FILL");
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return next();
        }
    }
}
