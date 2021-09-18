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

import com.ibasco.agql.core.AbstractPacketBuilder;
import com.ibasco.agql.protocols.valve.steam.master.packets.MasterServerResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MasterServerPacketBuilder extends AbstractPacketBuilder<MasterServerPacket> {

    private static Logger log = LoggerFactory.getLogger(MasterServerPacketBuilder.class);

    public MasterServerPacketBuilder(ByteBufAllocator allocator) {
        super(allocator);
    }

    @Override
    public <T extends MasterServerPacket> T construct(ByteBuf data) {
        //Mark Index
        data.markReaderIndex();
        try {
            //Reset the index
            data.readerIndex(0);
            //Verify size
            if (data.readableBytes() <= 6)
                throw new IllegalStateException("Cannot continue processing buffer with less than or equal to 6 bytes");
            //Read header
            byte[] header = new byte[6];
            data.readBytes(header);
            //Read payload
            byte[] payload = new byte[data.readableBytes()];
            data.readBytes(payload);
            //Verify if packet header is valid
            MasterServerResponsePacket packet = new MasterServerResponsePacket();
            packet.setHeader(header);
            packet.setPayload(payload);
            return (T) packet;
        } finally {
            data.resetReaderIndex();
        }
    }

    @Override
    public byte[] deconstruct(MasterServerPacket packet) {
        if (packet == null)
            throw new IllegalArgumentException("Invalid packet specified in the arguments.");

        byte[] payload = packet.getPayload();
        byte[] packetHeader = packet.getPacketHeader();

        int payloadSize = payload != null ? payload.length : 0;
        int packetHeaderSize = packetHeader != null ? packetHeader.length : 0;
        int packetSize = packetHeaderSize + payloadSize;

        //Allocate our buffer
        final ByteBuf buf = createBuffer(packetSize);
        byte[] data;

        try {
            //Include Packet Header
            if (packetHeaderSize > 0)
                buf.writeBytes(packetHeader);
            //Include Payload (if available)
            if (payloadSize > 0)
                buf.writeBytes(payload);
            //Store the buffer into a byte array
            data = new byte[buf.readableBytes()];
            if (data.length > 0) {
                buf.readBytes(data);
            }
        } finally {
            buf.release();
        }

        log.debug("Constructing '{}' with total of {} bytes of data", packet.getClass().getSimpleName(), data.length);

        //Return the backing array representation
        return data;
    }

}
