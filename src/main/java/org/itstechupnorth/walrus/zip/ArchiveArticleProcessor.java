/*
 *  Copyright 2010import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import walrus.base.ArticleBuffer;
import walrus.base.ArticleProcessor;
 You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.itstechupnorth.walrus.zip;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.itstechupnorth.walrus.base.ArticleBuffer;
import org.itstechupnorth.walrus.base.ArticleProcessor;


public class ArchiveArticleProcessor extends ArticleProcessor {

    private static final int DEFAULT_NUMBER_OF_ARCHIVES = 128;
    private final Random random;
    private final int numberOfArchives;
    private final ArchiveSaver[] archives;

    public ArchiveArticleProcessor(final File directory,
            final int numberOfArchives) {
        directory.mkdirs();
        archives = new ArchiveSaver[numberOfArchives];
        for (int i = 0; i < numberOfArchives; i++) {
            archives[i] = new ArchiveSaver(new File(directory,
                    "article-archive-" + i + ".zip"));
        }
        this.numberOfArchives = numberOfArchives;
        random = new Random();
    }

    public ArchiveArticleProcessor(String directoryName) {
        this(new File(directoryName), DEFAULT_NUMBER_OF_ARCHIVES);
    }

    @Override
    public ArticleBuffer process(ArticleBuffer buffer) throws Exception {
        final ArchiveSaver archive = archives[random.nextInt(numberOfArchives)];
        // System.out.println("Saving " + buffer);
        archive.save(buffer);
        // System.out.println("Saved " + buffer);
        return buffer;
    }

    @Override
    public void close() {
        for (final ArchiveSaver archive : archives) {
            try {
                archive.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "ArchiveArticleProcessor [archives=" + Arrays.toString(archives)
                + ", numberOfArchives=" + numberOfArchives + ", random="
                + random + "]";
    }
}
