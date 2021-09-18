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

package com.ibasco.agql.protocols.valve.steam.master;

import com.ibasco.agql.core.transport.NettyChannelInitializer;
import com.ibasco.agql.core.transport.NettyTransport;
import com.ibasco.agql.core.transport.handlers.ErrorHandler;
import com.ibasco.agql.protocols.valve.steam.master.handlers.MasterServerPacketDecoder;
import com.ibasco.agql.protocols.valve.steam.master.handlers.MasterServerRequestEncoder;
import io.netty.channel.Channel;

import java.util.function.BiConsumer;

public class MasterServerChannelInitializer implements NettyChannelInitializer {

    private BiConsumer<MasterServerResponse, Throwable> responseHandler;

    public MasterServerChannelInitializer(BiConsumer<MasterServerResponse, Throwable> responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public void initializeChannel(Channel channel, NettyTransport transport) {
        MasterServerPacketBuilder builder = new MasterServerPacketBuilder(transport.getAllocator());
        channel.pipeline().addLast(new ErrorHandler());
        channel.pipeline().addLast(new MasterServerRequestEncoder(builder));
        channel.pipeline().addLast(new MasterServerPacketDecoder(responseHandler, builder));
    }
}
