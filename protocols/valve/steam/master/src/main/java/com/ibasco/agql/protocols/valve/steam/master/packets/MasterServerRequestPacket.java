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

package com.ibasco.agql.protocols.valve.steam.master.packets;

import com.ibasco.agql.core.utils.ServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerPacket;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.net.InetSocketAddress;

public class MasterServerRequestPacket extends MasterServerPacket {

    /**
     * The master source address for Source Servers
     */
    public static final InetSocketAddress SOURCE_MASTER = new InetSocketAddress("hl2master.steampowered.com", 27011);

    public static final InetSocketAddress GOLDSRC_MASTER = new InetSocketAddress("hl1master.steampowered.com", 27010);

    private static final byte MASTER_SERVER_REQUEST_HEADER = 0x31;

    private byte region;

    private ServerFilter filter;

    private String startIp;

    public MasterServerRequestPacket() {

    }

    public MasterServerRequestPacket(MasterServerRegion region, ServerFilter filter, InetSocketAddress startIp) {
        this(region, filter, new StringBuilder().append(startIp.getAddress().getHostAddress()).append(":").append(startIp.getPort()).toString());
    }

    public MasterServerRequestPacket(MasterServerRegion region, ServerFilter filter, String startIp) {
        setHeader(MASTER_SERVER_REQUEST_HEADER);
        this.region = region.getHeader();
        this.filter = filter;
        this.startIp = startIp;
    }

    public byte getRegion() {
        return region;
    }

    public void setRegion(byte region) {
        this.region = region;
    }

    public ServerFilter getFilter() {
        return filter;
    }

    public void setFilter(ServerFilter filter) {
        this.filter = filter;
    }

    public String getStartIp() {
        return startIp;
    }

    public void setStartIp(String startIp) {
        this.startIp = startIp;
    }

    @Override
    public byte[] getPayload() {
        String filterString = this.filter.toString();
        int payloadSize = (3 + filterString.length() + (this.startIp.length()));
        final ByteBuf payload = PooledByteBufAllocator.DEFAULT.buffer(payloadSize);
        try {
            payload.writeByte(getRegion());
            payload.writeBytes(getStartIp().getBytes());
            payload.writeByte(0); //terminating byte
            payload.writeBytes(filterString.getBytes());
            byte[] payloadBytes = new byte[payload.readableBytes()];
            payload.readBytes(payloadBytes);
            return payloadBytes;
        } finally {
            payload.release();
        }
    }
}
