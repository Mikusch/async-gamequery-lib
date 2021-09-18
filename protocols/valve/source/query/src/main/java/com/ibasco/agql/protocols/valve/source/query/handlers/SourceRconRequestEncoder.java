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

import com.ibasco.agql.core.transport.handlers.AbstractRequestEncoder;
import com.ibasco.agql.protocols.valve.source.query.SourceRconPacket;
import com.ibasco.agql.protocols.valve.source.query.SourceRconPacketBuilder;
import com.ibasco.agql.protocols.valve.source.query.SourceRconRequest;
import com.ibasco.agql.protocols.valve.source.query.packets.request.SourceRconTermRequestPacket;
import com.ibasco.agql.protocols.valve.source.query.request.SourceRconAuthRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class SourceRconRequestEncoder extends AbstractRequestEncoder<SourceRconRequest, SourceRconPacket> {
    private static final Logger log = LoggerFactory.getLogger(SourceRconRequestEncoder.class);

    private boolean sendTerminatorPackets = true;

    public SourceRconRequestEncoder(SourceRconPacketBuilder builder, boolean sendTerminatorPackets) {
        super(builder);
        this.sendTerminatorPackets = sendTerminatorPackets;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, SourceRconRequest msg, List<Object> out) throws Exception {
        ByteBuf rconRequestPacket = builder.deconstructAsBuffer((SourceRconPacket) msg.getMessage());

        if (log.isDebugEnabled()) {
            log.debug("Encoding Rcon Request: \n{}", ByteBufUtil.prettyHexDump(rconRequestPacket));
        }

        out.add(rconRequestPacket);

        //Send rcon-terminator except if it is an authentication request packet
        if (this.sendTerminatorPackets && !(msg instanceof SourceRconAuthRequest)) {
            ByteBuf terminatorPacket = builder.deconstructAsBuffer(new SourceRconTermRequestPacket());
            log.debug("Sending RCON Terminator ({} bytes): \n{}", terminatorPacket.readableBytes(), ByteBufUtil.prettyHexDump(terminatorPacket));
            out.add(terminatorPacket);
        }
    }
}
