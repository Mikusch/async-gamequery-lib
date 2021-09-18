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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

abstract public class AbstractPacketBuilder<T extends AbstractPacket> implements PacketBuilder<T> {
    private ByteBufAllocator allocator;

    public AbstractPacketBuilder(ByteBufAllocator allocator) {
        this.allocator = allocator;
    }

    public ByteBufAllocator getAllocator() {
        return allocator;
    }

    public void setAllocator(ByteBufAllocator allocator) {
        this.allocator = allocator;
    }

    @Override
    public ByteBuf deconstructAsBuffer(T packet) {
        byte[] rawPacket = deconstruct(packet);
        return allocator.buffer(rawPacket.length).writeBytes(rawPacket);
    }

    protected ByteBuf createBuffer(int capacity) {
        return this.allocator.buffer(capacity);
    }
}
