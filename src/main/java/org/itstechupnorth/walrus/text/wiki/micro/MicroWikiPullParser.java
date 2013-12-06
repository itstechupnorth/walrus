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
package org.itstechupnorth.walrus.text.wiki.micro;

import java.nio.CharBuffer;

import org.apache.commons.lang.CharUtils;

public class MicroWikiPullParser implements WikiPullParser {

    private enum Token {
        CLOSED_CURLY(WikiState.CLOSE_CURLY, 2), OPEN_CURLY(
                WikiState.OPEN_CURLY, 2), CLOSED_SQUARE(WikiState.CLOSE_SQUARE,
                2), OPEN_SQUARE(WikiState.OPEN_SQUARE, 2), CR(
                WikiState.EMPTY_LINE, 2), LF(WikiState.EMPTY_LINE, 2), CRLF(
                WikiState.EMPTY_LINE, 4), CRLFCR(WikiState.EMPTY_LINE, 4), EQ(
                null, 0), EQEQ(WikiState.TITLE, 2), EQEQEQ(WikiState.SUBTITLE,
                3), EQ4(WikiState.SUBSUBTITLE, 4), EQ5(WikiState.SUB3TITLE, 5), EQ6(
                WikiState.SUB4TITLE, 6), HASH(WikiState.HASH, 1), STAR(
                WikiState.STAR, 1), PIPE(WikiState.PIPE, 1), OTHER(null, 0);

        private final WikiState state;
        private final int length;

        Token(final WikiState state, final int length) {
            this.state = state;
            this.length = length;
        }

        public WikiState state() {
            return state;
        }

        public int length() {
            return length;
        }
    }

    private final CharBuffer buffer;
    private int textEnd = 0;

    public MicroWikiPullParser(String string) {
        this(CharBuffer.wrap(string).asReadOnlyBuffer());
    }

    public MicroWikiPullParser(CharBuffer buffer) {
        super();
        this.buffer = buffer;
    }

    /**
     * @see org.itstechupnorth.walrus.text.wiki.micro.WikiPullParser#clear()
     */
    public void clear() {
        buffer.clear();
    }

    /**
     * @see org.itstechupnorth.walrus.text.wiki.micro.WikiPullParser#text()
     */
    public CharSequence text() {
        final int currentPosition = buffer.position();
        final int markedPosition = buffer.reset().position();
        final CharSequence result;
        if (textEnd > markedPosition) {
            result = buffer.subSequence(0, textEnd - markedPosition);
        } else {
            result = "";
        }
        buffer.position(currentPosition);
        return result;
    }

    /**
     * @see org.itstechupnorth.walrus.text.wiki.micro.WikiPullParser#next()
     */
    public WikiState next() {
        buffer.mark();
        return seekNext();
    }

    private WikiState seekNext() {
        if (buffer.hasRemaining()) {
            Token last = Token.OTHER;
            while (buffer.hasRemaining()) {
                char next = buffer.get();
                if (next != '=') {
                    if (last == Token.EQEQ) {
                        backstep();
                        return next(Token.EQEQ);
                    } else if (last == Token.EQEQEQ) {
                        backstep();
                        return next(Token.EQEQEQ);
                    } else if (last == Token.EQ4) {
                        backstep();
                        return next(Token.EQ4);
                    } else if (last == Token.EQ5) {
                        backstep();
                        return next(Token.EQ5);
                    } else if (last == Token.EQ6) {
                        backstep();
                        return next(Token.EQ6);
                    }
                }
                switch (next) {
                    case '}':
                        if (last == Token.CLOSED_CURLY) {
                            return next(Token.CLOSED_CURLY);
                        }
                        last = Token.CLOSED_CURLY;
                        break;
                    case '{':
                        if (last == Token.OPEN_CURLY) {
                            return next(Token.OPEN_CURLY);
                        }
                        last = Token.OPEN_CURLY;
                        break;
                    case ']':
                        if (last == Token.CLOSED_SQUARE) {
                            return next(Token.CLOSED_SQUARE);
                        }
                        last = Token.CLOSED_SQUARE;
                        break;
                    case '[':
                        if (last == Token.OPEN_SQUARE) {
                            return next(Token.OPEN_SQUARE);
                        }
                        last = Token.OPEN_SQUARE;
                        break;
                    case '#':
                        return next(Token.HASH);
                    case '*':
                        return next(Token.STAR);
                    case '|':
                        return next(Token.PIPE);
                    case CharUtils.CR:
                        if (last == Token.CR) {
                            return next(Token.CR);
                        } else if (last == Token.CRLF) {
                            last = Token.CRLFCR;
                        } else {
                            last = Token.CR;
                        }
                        break;
                    case CharUtils.LF:
                        if (last == Token.LF) {
                            return next(Token.LF);
                        } else if (last == Token.CRLFCR) {
                            return next(Token.CRLFCR);
                        } else if (last == Token.CR) {
                            last = Token.CRLF;
                        } else {
                            last = Token.LF;
                        }
                        break;
                    case '=':
                        if (last == Token.EQ) {
                            last = Token.EQEQ;
                        } else if (last == Token.EQEQ) {
                            last = Token.EQEQEQ;
                        } else if (last == Token.EQEQEQ) {
                            last = Token.EQ4;
                        } else if (last == Token.EQ4) {
                            last = Token.EQ5;
                        } else if (last == Token.EQ5) {
                            last = Token.EQ6;
                        } else {
                            last = Token.EQ;
                        }
                        break;
                    default:
                        last = Token.OTHER;
                        break;
                }

            }
            if (last == Token.EQEQ) {
                return next(Token.EQEQ);
            } else if (last == Token.EQEQEQ) {
                return next(Token.EQEQEQ);
            } else if (last == Token.EQ4) {
                return next(Token.EQ4);
            } else if (last == Token.EQ5) {
                return next(Token.EQ5);
            } else if (last == Token.EQ6) {
                return next(Token.EQ6);
            }
        }
        textEnd = buffer.position();
        return WikiState.END;
    }

    private WikiState next(Token token) {
        textEnd = buffer.position() - token.length();
        return token.state();
    }

    private void backstep() {
        buffer.position(buffer.position() - 1);
    }
}
