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

package com.ibasco.agql.core;

import com.ibasco.agql.core.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

abstract public class AbstractPacket implements Packet {
    private byte[] header = new byte[0];
    private byte[] payload = new byte[0];
    private byte[] trailer = new byte[0];

    @Override
    public byte getSingleBytePacketHeader() {
        return getPacketHeader() != null && getPacketHeader().length >= 1 ? getPacketHeader()[0] : 0;
    }

    @Override
    public byte[] getPacketHeader() {
        return this.header;
    }

    @Override
    public byte[] getPayload() {
        return this.payload;
    }

    public ByteBuf getPayloadBuffer() {
        return Unpooled.copiedBuffer(getPayload());
    }

    @Override
    public byte[] getTrailer() {
        return this.trailer;
    }

    public void setHeader(byte header) {
        setHeader(new byte[]{header});
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public void setTrailer(byte[] trailer) {
        this.trailer = trailer;
    }

    public ToStringBuilder toStringBuilder() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("class", this.getClass().getSimpleName())
                .append("header", ByteUtils.bytesToHex(getPacketHeader()));
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }
}
