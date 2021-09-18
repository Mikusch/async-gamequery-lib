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

package com.ibasco.agql.core.messenger;

import com.ibasco.agql.core.AbstractWebRequest;
import com.ibasco.agql.core.AbstractWebResponse;
import com.ibasco.agql.core.Messenger;
import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.transport.http.AsyncHttpTransport;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;
import org.asynchttpclient.filter.ThrottleRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * A Messenger responsible for handling Web Requests and Responses.
 */
//TODO: Implement a generic request/response lookup service
public class WebMessenger<R extends AbstractWebRequest, S extends AbstractWebResponse> implements Messenger<R, S> {

    private static final Logger log = LoggerFactory.getLogger(WebMessenger.class);

    private Transport<Request> transport;

    private Function<Response, S> responseFactory;

    public WebMessenger(Function<Response, S> responseFactory) {
        this(responseFactory, null);
    }

    public WebMessenger(Function<Response, S> responseFactory, ExecutorService executorService) {
        DefaultAsyncHttpClientConfig.Builder configBuilder = new DefaultAsyncHttpClientConfig.Builder();
        configBuilder.setKeepAlive(true);
        configBuilder.addRequestFilter(new ThrottleRequestFilter(40));

        if (executorService != null) {
            EventLoopGroup eventLoopGroup;
            if (Epoll.isAvailable()) {
                eventLoopGroup = new EpollEventLoopGroup(0, executorService);
            } else {
                eventLoopGroup = new NioEventLoopGroup(0, executorService);
            }
            configBuilder.setEventLoopGroup(eventLoopGroup);
        }
        this.transport = new AsyncHttpTransport(configBuilder.build());
        this.responseFactory = responseFactory;
    }

    @Override
    public CompletableFuture<S> send(R request) {
        log.debug("Sending request with url : {}", request.getMessage().getUri());
        CompletableFuture<Response> res = transport.send(request.getMessage());
        //transform the raw Response type to an instance of AbstractWebResponse using the supplied factory method
        return res.thenApply(responseFactory);
    }

    public Function<Response, S> getResponseFactory() {
        return responseFactory;
    }

    public void setResponseFactory(Function<Response, S> responseFactory) {
        this.responseFactory = responseFactory;
    }

    @Override
    public void accept(S res, Throwable throwable) {
        //do nothing
    }

    @Override
    public void close() throws IOException {
        transport.close();
    }
}
