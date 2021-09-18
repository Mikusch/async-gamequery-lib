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

package com.ibasco.agql.core.transport.udp;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.enums.ChannelType;
import com.ibasco.agql.core.transport.NettyPooledTransport;
import com.ibasco.agql.core.transport.pool.ConnectionlessChannelPool;
import io.netty.channel.pool.ChannelPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * A pooled udp transport implementation
 *
 * @param <M> A type extending {@link AbstractRequest}
 */
public class NettyPooledUdpTransport<M extends AbstractRequest> extends NettyPooledTransport<M, Class<?>> {
    private static final Logger log = LoggerFactory.getLogger(NettyPooledUdpTransport.class);

    public NettyPooledUdpTransport(ChannelType channelType) {
        this(channelType, null);
    }

    public NettyPooledUdpTransport(ChannelType channelType, ExecutorService executor) {
        super(channelType, executor);
    }

    @Override
    public Class<?> createKey(M message) {
        return message.getClass();
    }

    @Override
    public ChannelPool createChannelPool(Class<?> key) {
        log.debug("Creating Channel Pool For : {}", key.getSimpleName());
        return new ConnectionlessChannelPool(getBootstrap(), channelPoolHandler);
    }
}
