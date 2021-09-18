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
import com.ibasco.agql.core.exceptions.MessengerException;
import com.ibasco.agql.core.messenger.GameServerMessenger;
import com.ibasco.agql.core.transport.tcp.NettyPooledTcpTransport;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconRequestType;
import com.ibasco.agql.protocols.valve.source.query.request.SourceRconAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.request.SourceRconCmdRequest;
import com.ibasco.agql.protocols.valve.source.query.response.SourceRconAuthResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceRconCmdResponse;
import io.netty.channel.ChannelOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class SourceRconMessenger extends GameServerMessenger<SourceRconRequest, SourceRconResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconMessenger.class);

    private Map<Integer, SourceRconRequestType> requestTypeMap = new LinkedHashMap<>();

    private boolean terminatingPacketsEnabled;

    public SourceRconMessenger(boolean terminatingPacketsEnabled) {
        this(terminatingPacketsEnabled, null);
    }

    public SourceRconMessenger(boolean terminatingPacketsEnabled, ExecutorService executorService) {
        super(new SourceRconSessionIdFactory(), ProcessingMode.SYNCHRONOUS, executorService);
        this.terminatingPacketsEnabled = terminatingPacketsEnabled;
    }

    @Override
    protected Transport<SourceRconRequest> createTransportService() {
        NettyPooledTcpTransport<SourceRconRequest> transport = new NettyPooledTcpTransport<>(ChannelType.NIO_TCP, getExecutorService());
        transport.setChannelInitializer(new SourceRconChannelInitializer(this));
        transport.addChannelOption(ChannelOption.SO_SNDBUF, 1048576 * 4);
        transport.addChannelOption(ChannelOption.SO_RCVBUF, 1048576 * 4);
        transport.addChannelOption(ChannelOption.SO_KEEPALIVE, true);
        transport.addChannelOption(ChannelOption.TCP_NODELAY, true);
        return transport;
    }

    @Override
    public CompletableFuture<SourceRconResponse> send(SourceRconRequest request) {
        final int requestId = request.getRequestId();

        SourceRconRequestType type = getRequestType(request);

        //Make sure we have a request type
        if (type == null)
            throw new MessengerException("Unrecognized rcon request");

        //Add the request type to the map
        requestTypeMap.put(requestId, getRequestType(request));

        final CompletableFuture<SourceRconResponse> futureResponse = super.send(request);
        //Make sure to remove the requestId once the response future is completed
        futureResponse.whenComplete((response, error) -> {
            log.debug("Removing request id '{}' from type map", requestId);
            requestTypeMap.remove(requestId);
        });
        return futureResponse;
    }

    public boolean isTerminatingPacketsEnabled() {
        return terminatingPacketsEnabled;
    }

    public SourceRconRequestType getRequestType(SourceRconRequest request) {
        if (request instanceof SourceRconAuthRequest) {
            return SourceRconRequestType.AUTH;
        } else if (request instanceof SourceRconCmdRequest) {
            return SourceRconRequestType.COMMAND;
        }
        return null;
    }

    public Map<Integer, SourceRconRequestType> getRequestTypeMap() {
        return requestTypeMap;
    }

    @Override
    public void configureMappings(Map<Class<? extends SourceRconRequest>, Class<? extends SourceRconResponse>> map) {
        map.put(SourceRconAuthRequest.class, SourceRconAuthResponse.class);
        map.put(SourceRconCmdRequest.class, SourceRconCmdResponse.class);
    }

    @Override
    public void accept(SourceRconResponse response, Throwable error) {
        super.accept(response, error);
    }
}
