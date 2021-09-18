/*
 * Copyright 2021 Rafael Luis L. Ibasco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

abstract public class AbstractClient<R extends AbstractRequest,
        S extends AbstractResponse,
        M extends AbstractMessenger<R, S>>
        implements Client<R, S> {

    private M messenger;

    private static final Logger log = LoggerFactory.getLogger(AbstractClient.class);

    public AbstractClient(M messenger) {
        this.messenger = messenger;
    }

    @Override
    public <V> CompletableFuture<V> sendRequest(R message) {
        log.debug("Client '{}' Sending request : {}", this.getClass().getSimpleName(), message);
        //Send the request then transform the result once a response is received
        return messenger.send(message).thenApply(this::convertToResultType);
    }

    @SuppressWarnings("unchecked")
    private <V> V convertToResultType(S message) {
        return (V) message.getMessage();
    }

    protected M getMessenger() {
        return messenger;
    }

    protected void setMessenger(M messenger) {
        this.messenger = messenger;
    }

    @Override
    public void close() throws IOException {
        log.debug("Shutting down messenger");
        if (messenger != null)
            messenger.close();
    }
}
