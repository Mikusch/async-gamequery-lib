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

import com.ibasco.agql.core.transport.handlers.AbstractRequestEncoder;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerPacket;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerPacketBuilder;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerRequest;
import com.ibasco.agql.protocols.valve.steam.master.packets.MasterServerRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import java.util.List;

/**
 * Created by raffy on 10/22/2016.
 */
public class MasterServerRequestEncoder extends AbstractRequestEncoder<MasterServerRequest, MasterServerPacket> {

    public MasterServerRequestEncoder(MasterServerPacketBuilder builder) {
        super(builder);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, MasterServerRequest request, List<Object> out) throws Exception {
        MasterServerRequestPacket packet = request.getMessage();
        byte[] deconstructedPacket = builder.deconstruct(packet);
        if (deconstructedPacket != null && deconstructedPacket.length > 0) {
            ByteBuf buffer = ctx.alloc().buffer(deconstructedPacket.length).writeBytes(deconstructedPacket);
            out.add(new DatagramPacket(buffer, request.recipient()));
        }
    }
}
