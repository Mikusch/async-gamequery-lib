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
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceRconPacketAssembler;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceRconPacketDecoder;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceRconRequestEncoder;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceRconResponseRouter;
import io.netty.channel.Channel;

class SourceRconChannelInitializer implements NettyChannelInitializer {

    private SourceRconMessenger rconMessenger;

    SourceRconChannelInitializer(SourceRconMessenger responseHandler) {
        this.rconMessenger = responseHandler;
    }

    @Override
    public void initializeChannel(Channel channel, NettyTransport transport) {
        SourceRconPacketBuilder rconBuilder = new SourceRconPacketBuilder(transport.getAllocator());
        channel.pipeline().addLast(new SourceRconRequestEncoder(rconBuilder, rconMessenger.isTerminatingPacketsEnabled()));
        channel.pipeline().addLast(new SourceRconPacketDecoder(rconMessenger.isTerminatingPacketsEnabled()));
        channel.pipeline().addLast(new SourceRconPacketAssembler(rconMessenger.getRequestTypeMap(), rconMessenger.isTerminatingPacketsEnabled()));
        channel.pipeline().addLast(new SourceRconResponseRouter(rconMessenger));
        channel.pipeline().addLast(new ErrorHandler());
    }
}
