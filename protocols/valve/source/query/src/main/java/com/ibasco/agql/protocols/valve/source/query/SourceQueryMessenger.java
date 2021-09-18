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

import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.enums.ChannelType;
import com.ibasco.agql.core.enums.ProcessingMode;
import com.ibasco.agql.core.messenger.GameServerMessenger;
import com.ibasco.agql.core.transport.udp.NettyPooledUdpTransport;
import com.ibasco.agql.protocols.valve.source.query.request.SourceChallengeRequest;
import com.ibasco.agql.protocols.valve.source.query.request.SourceInfoRequest;
import com.ibasco.agql.protocols.valve.source.query.request.SourcePlayerRequest;
import com.ibasco.agql.protocols.valve.source.query.request.SourceRulesRequest;
import com.ibasco.agql.protocols.valve.source.query.response.SourceChallengeResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceInfoResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourcePlayerResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceRulesResponse;
import io.netty.channel.ChannelOption;
import io.netty.channel.FixedRecvByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by raffy on 9/14/2016.
 */
public class SourceQueryMessenger extends GameServerMessenger<SourceServerRequest, SourceServerResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryMessenger.class);

    public SourceQueryMessenger() {
        //Use the default session manager
        this(ProcessingMode.ASYNCHRONOUS, null);
    }

    public SourceQueryMessenger(ProcessingMode processingMode, ExecutorService executorService) {
        //Use the default session manager
        super(ProcessingMode.ASYNCHRONOUS, executorService);
    }

    @Override
    protected Transport<SourceServerRequest> createTransportService() {
        NettyPooledUdpTransport<SourceServerRequest> transport = new NettyPooledUdpTransport<>(ChannelType.NIO_UDP, getExecutorService());
        transport.setChannelInitializer(new SourceQueryChannelInitializer(this));
        transport.addChannelOption(ChannelOption.SO_SNDBUF, 1048576);
        transport.addChannelOption(ChannelOption.SO_RCVBUF, 1048576);
        transport.addChannelOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1400));
        return transport;
    }

    @Override
    public void configureMappings(Map<Class<? extends SourceServerRequest>, Class<? extends SourceServerResponse>> map) {
        map.put(SourceInfoRequest.class, SourceInfoResponse.class);
        map.put(SourceChallengeRequest.class, SourceChallengeResponse.class);
        map.put(SourcePlayerRequest.class, SourcePlayerResponse.class);
        map.put(SourceRulesRequest.class, SourceRulesResponse.class);
    }
}
