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

package com.ibasco.agql.core.transport.handlers;

import com.ibasco.agql.core.AbstractPacket;
import com.ibasco.agql.core.AbstractPacketBuilder;
import com.ibasco.agql.core.AbstractRequest;
import io.netty.handler.codec.MessageToMessageEncoder;

abstract public class AbstractRequestEncoder<T extends AbstractRequest, P extends AbstractPacket> extends MessageToMessageEncoder<T> {
    protected AbstractPacketBuilder<P> builder;

    public AbstractRequestEncoder(AbstractPacketBuilder<P> builder) {
        this.builder = builder;
    }
}
