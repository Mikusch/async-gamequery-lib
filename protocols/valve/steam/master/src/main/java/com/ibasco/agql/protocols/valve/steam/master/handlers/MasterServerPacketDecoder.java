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

package com.ibasco.agql.protocols.valve.steam.master.handlers;

import com.ibasco.agql.protocols.valve.steam.master.MasterServerPacketBuilder;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerResponse;
import com.ibasco.agql.protocols.valve.steam.master.packets.MasterServerResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiConsumer;

public class MasterServerPacketDecoder extends MessageToMessageDecoder<DatagramPacket> {

    private static final Logger log = LoggerFactory.getLogger(MasterServerPacketDecoder.class);
    private BiConsumer<MasterServerResponse, Throwable> responseCallback;
    private MasterServerPacketBuilder builder;

    public MasterServerPacketDecoder(BiConsumer<MasterServerResponse, Throwable> responseCallback, MasterServerPacketBuilder builder) {
        this.responseCallback = responseCallback;
        this.builder = builder;
    }

    //TODO: maybe make a more generic version of this class?
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        //Create our response packet from the datagram we received
        final MasterServerResponsePacket packet = builder.construct(msg.content());
        if (packet != null) {
            final MasterServerResponse response = new MasterServerResponse();
            response.setSender(msg.sender());
            response.setRecipient(msg.recipient());
            response.setResponsePacket(packet);
            log.debug("Receiving Data '{}' from '{}' using Channel Id: {}", response.getClass().getSimpleName(), ctx.channel().remoteAddress(), ctx.channel().id());
            //Pass the message back to the messenger
            responseCallback.accept(response, null);
            return;
        }
        throw new IllegalStateException("No response packet found for the incoming datagram");
    }
}
