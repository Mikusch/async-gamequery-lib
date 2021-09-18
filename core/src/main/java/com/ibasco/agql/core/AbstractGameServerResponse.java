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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.net.InetSocketAddress;

abstract public class AbstractGameServerResponse<T extends Decodable<U>, U> extends AbstractResponse<U> {
    private T responsePacket;

    public AbstractGameServerResponse(InetSocketAddress sender, T packet) {
        super(sender);
        this.responsePacket = packet;
    }

    public T getResponsePacket() {
        return responsePacket;
    }

    public void setResponsePacket(T responsePacket) {
        this.responsePacket = responsePacket;
    }

    @Override
    public U getMessage() {
        return responsePacket.toObject();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("type", this.getClass().getSimpleName())
                .append("sender", sender())
                .append("responsePacket", responsePacket)
                .toString();
    }
}
