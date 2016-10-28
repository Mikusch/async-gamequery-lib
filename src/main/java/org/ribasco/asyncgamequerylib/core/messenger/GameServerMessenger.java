/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package org.ribasco.asyncgamequerylib.core.messenger;

import org.ribasco.asyncgamequerylib.core.AbstractGameServerRequest;
import org.ribasco.asyncgamequerylib.core.AbstractGameServerResponse;
import org.ribasco.asyncgamequerylib.core.AbstractMessenger;
import org.ribasco.asyncgamequerylib.core.enums.ProcessingMode;
import org.ribasco.asyncgamequerylib.core.session.AbstractSessionIdFactory;
import org.ribasco.asyncgamequerylib.core.session.SessionManager;

import java.util.concurrent.ExecutorService;

/**
 * Messenger using the UDP Transport protocol
 */
abstract public class GameServerMessenger<A extends AbstractGameServerRequest, B extends AbstractGameServerResponse>
        extends AbstractMessenger<A, B> {

    public GameServerMessenger(ProcessingMode processingMode) {
        super(processingMode);
    }

    public GameServerMessenger(AbstractSessionIdFactory keyFactory, ProcessingMode processingMode) {
        super(keyFactory, processingMode);
    }

    public GameServerMessenger(SessionManager sessionManager, ProcessingMode processingMode, int initQueueCapacity, ExecutorService executorService) {
        super(sessionManager, processingMode, initQueueCapacity, executorService);
    }
}
