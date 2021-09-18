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

package com.ibasco.agql.protocols.valve.source.query.handlers;

import com.ibasco.agql.core.exceptions.ResponseException;
import com.ibasco.agql.protocols.valve.source.query.SourceRconResponse;
import com.ibasco.agql.protocols.valve.source.query.SourceRconResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.exceptions.SourceRconExecutionException;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceRconAuthResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.packets.response.SourceRconCmdResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.response.SourceRconAuthResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceRconCmdResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.function.BiConsumer;

public class SourceRconResponseRouter extends SimpleChannelInboundHandler<Object> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconResponseRouter.class);

    private BiConsumer<SourceRconResponse, Throwable> responseCallback;

    public SourceRconResponseRouter(BiConsumer<SourceRconResponse, Throwable> responseCallback) {
        this.responseCallback = responseCallback;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        SourceRconResponse response = null;

        if (msg instanceof SourceRconAuthResponsePacket) {
            response = new SourceRconAuthResponse();
        } else if (msg instanceof SourceRconCmdResponsePacket) {
            response = new SourceRconCmdResponse();
        } else if (msg instanceof ResponseException) {
            log.debug("Received response exception. Passing to messenger");
            responseCallback.accept(null, (ResponseException) msg);
            return;
        }

        if (response != null) {
            SourceRconResponsePacket packet = (SourceRconResponsePacket) msg;
            response.setSender((InetSocketAddress) ctx.channel().remoteAddress());
            response.setRecipient((InetSocketAddress) ctx.channel().localAddress());
            response.setRequestId(packet.getId());
            response.setResponsePacket(packet);
            log.debug("Response Processed. Sending back to the messenger : '{}={}'", response.getClass().getSimpleName(), response.sender());
            responseCallback.accept(response, null);
        } else
            responseCallback.accept(null, new SourceRconExecutionException("Invalid response received"));
    }
}
