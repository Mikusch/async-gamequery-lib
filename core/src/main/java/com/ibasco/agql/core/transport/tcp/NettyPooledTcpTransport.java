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

package com.ibasco.agql.core.transport.tcp;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.enums.ChannelType;
import com.ibasco.agql.core.transport.ChannelAttributes;
import com.ibasco.agql.core.transport.NettyPooledTransport;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * <p>A Pooled TCP Transport implementation which creates and reuse channels stored within a pool map.
 * {@link InetSocketAddress} is used as the key for the {@link io.netty.channel.pool.ChannelPoolMap} implementation</p>
 *
 * @param <M> A type of {@link AbstractRequest} that will be used as a lookup reference for our key
 */
public class NettyPooledTcpTransport<M extends AbstractRequest> extends NettyPooledTransport<M, InetSocketAddress> {

    private static final Logger log = LoggerFactory.getLogger(NettyPooledTcpTransport.class);

    public NettyPooledTcpTransport(ChannelType channelType) {
        this(channelType, null);
    }

    public NettyPooledTcpTransport(ChannelType channelType, ExecutorService executor) {
        super(channelType, executor);
    }

    @Override
    public InetSocketAddress createKey(M message) {
        return message.recipient();
    }

    @Override
    public ChannelPool createChannelPool(InetSocketAddress key) {
        return new SimpleChannelPool(getBootstrap().remoteAddress(key), channelPoolHandler);
    }

    @Override
    public CompletableFuture<Channel> getChannel(M message) {
        return super.getChannel(message).thenApply(c -> {
            c.attr(ChannelAttributes.LAST_REQUEST_SENT).set(message);
            return c;
        });
    }
}
