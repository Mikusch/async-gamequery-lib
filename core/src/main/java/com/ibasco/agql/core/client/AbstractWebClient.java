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

package com.ibasco.agql.core.client;

import com.ibasco.agql.core.AbstractWebRequest;
import com.ibasco.agql.core.AbstractWebResponse;
import com.ibasco.agql.core.Client;
import com.ibasco.agql.core.messenger.WebMessenger;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * A generic Http Client base
 *
 * @param <R> Type of {@link AbstractWebRequest}
 * @param <S> Type of {@link AbstractWebResponse}
 */
abstract class AbstractWebClient<R extends AbstractWebRequest, S extends AbstractWebResponse>
        implements Client<R, S> {

    private WebMessenger<R, S> messenger;

    AbstractWebClient(ExecutorService executorService) {
        this.messenger = createWebMessenger(executorService);
    }

    abstract public WebMessenger<R, S> createWebMessenger(ExecutorService executorService);

    @Override
    @SuppressWarnings("unchecked")
    public <V> CompletableFuture<V> sendRequest(R message) {
        return (CompletableFuture<V>) messenger.send(message);
    }

    @Override
    public void close() throws IOException {
        messenger.close();
    }
}
