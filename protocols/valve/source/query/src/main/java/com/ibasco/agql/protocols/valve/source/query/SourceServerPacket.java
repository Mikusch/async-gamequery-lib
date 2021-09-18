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

import com.ibasco.agql.core.AbstractPacket;
import com.ibasco.agql.core.utils.ByteUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by raffy on 9/1/2016.
 */
public abstract class SourceServerPacket extends AbstractPacket {
    private byte[] protocolHeader = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    public byte[] getProtocolHeader() {
        return this.protocolHeader;
    }

    public void setProtocolHeader(byte[] protocolHeader) {
        this.protocolHeader = protocolHeader;
    }

    public void setProtocolHeader(byte protocolHeader) {
        this.protocolHeader = new byte[]{protocolHeader};
    }

    @Override
    public ToStringBuilder toStringBuilder() {
        return super.toStringBuilder().append("protocol_header", ByteUtils.bytesToHex(this.protocolHeader));
    }
}
