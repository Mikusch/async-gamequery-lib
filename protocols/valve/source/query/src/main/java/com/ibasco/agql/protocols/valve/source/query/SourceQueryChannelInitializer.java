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

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.core.transport.NettyChannelInitializer;
import com.ibasco.agql.core.transport.NettyTransport;
import com.ibasco.agql.core.transport.handlers.ErrorHandler;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceQueryPacketAssembler;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceQueryPacketDecoder;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceQueryRequestEncoder;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class SourceQueryChannelInitializer implements NettyChannelInitializer {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryChannelInitializer.class);

    private BiConsumer<SourceServerResponse, Throwable> responseHandler;

    public SourceQueryChannelInitializer(BiConsumer<SourceServerResponse, Throwable> responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public void initializeChannel(Channel channel, NettyTransport transport) {
        SourcePacketBuilder builder = new SourcePacketBuilder(transport.getAllocator());
        channel.pipeline().addLast(new SourceQueryRequestEncoder(builder));
        channel.pipeline().addLast(new SourceQueryPacketAssembler());
        channel.pipeline().addLast(new SourceQueryPacketDecoder(responseHandler, builder));
        channel.pipeline().addLast(new ErrorHandler());
    }
}
