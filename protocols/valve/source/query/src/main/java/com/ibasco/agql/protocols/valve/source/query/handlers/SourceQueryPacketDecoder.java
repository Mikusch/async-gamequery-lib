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

import com.ibasco.agql.core.exceptions.AsyncGameLibCheckedException;
import com.ibasco.agql.core.exceptions.NoResponseFoundForPacket;
import com.ibasco.agql.protocols.valve.source.query.SourcePacketBuilder;
import com.ibasco.agql.protocols.valve.source.query.SourceResponseFactory;
import com.ibasco.agql.protocols.valve.source.query.SourceResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.SourceServerResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Decodes the packet and wraps it to a response object
 */
public class SourceQueryPacketDecoder extends MessageToMessageDecoder<DatagramPacket> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryPacketDecoder.class);

    private SourcePacketBuilder builder;
    private BiConsumer<SourceServerResponse, Throwable> responseHandler;

    public SourceQueryPacketDecoder(BiConsumer<SourceServerResponse, Throwable> responseHandler, SourcePacketBuilder builder) {
        this.builder = builder;
        this.responseHandler = responseHandler;
    }

    @Override
    @SuppressWarnings({"unchecked", "Duplicates"})
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        //Create our response packet from the datagram we received
        final SourceResponsePacket packet = builder.construct(msg.content());
        if (packet != null) {
            try {
                SourceServerResponse response = SourceResponseFactory.createResponseFrom(packet);
                if (response != null) {
                    response.setSender(msg.sender());
                    response.setRecipient(msg.recipient());
                    response.setResponsePacket(packet);
                    log.debug("Receiving Data '{}' from '{}' using Channel Id: {}", response.getClass().getSimpleName(), ctx.channel().remoteAddress(), ctx.channel().id());
                    //Pass the message back to the messenger
                    responseHandler.accept(response, null);
                    return;
                }
            } catch (Exception e) {
                responseHandler.accept(null, new AsyncGameLibCheckedException("Error while decoding source query response", e));
            }
        }
        throw new NoResponseFoundForPacket("Could not find a response handler for the received datagram packet", packet);
    }
}
