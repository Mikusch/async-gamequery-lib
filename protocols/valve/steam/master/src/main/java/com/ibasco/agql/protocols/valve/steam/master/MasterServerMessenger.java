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

import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.enums.ChannelType;
import com.ibasco.agql.core.enums.ProcessingMode;
import com.ibasco.agql.core.messenger.GameServerMessenger;
import com.ibasco.agql.core.transport.udp.NettyPooledUdpTransport;
import io.netty.channel.ChannelOption;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by raffy on 10/22/2016.
 */
public class MasterServerMessenger extends GameServerMessenger<MasterServerRequest, MasterServerResponse> {

    public MasterServerMessenger() {
        this(null);
    }

    public MasterServerMessenger(ExecutorService executorService) {
        super(ProcessingMode.ASYNCHRONOUS, executorService);
    }

    @Override
    protected Transport<MasterServerRequest> createTransportService() {
        NettyPooledUdpTransport<MasterServerRequest> transport = new NettyPooledUdpTransport<>(ChannelType.NIO_UDP, getExecutorService());
        //Set our channel initializer
        transport.setChannelInitializer(new MasterServerChannelInitializer(this));
        //Channel Options
        transport.addChannelOption(ChannelOption.SO_SNDBUF, 1048576);
        transport.addChannelOption(ChannelOption.SO_RCVBUF, 1048576 * 8);
        return transport;
    }

    @Override
    public void configureMappings(Map<Class<? extends MasterServerRequest>, Class<? extends MasterServerResponse>> map) {
        map.put(MasterServerRequest.class, MasterServerResponse.class);
    }
}
