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

import com.ibasco.agql.core.AbstractGameServerResponse;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/24/2016.
 */
public abstract class SourceRconResponse<T, P extends SourceRconResponsePacket<T>>
        extends AbstractGameServerResponse<P, T>
        implements SourceRconMessage {

    private int requestId;

    public SourceRconResponse(InetSocketAddress sender, P packet, int requestId) {
        super(sender, packet);
        this.requestId = requestId;
    }

    @Override
    public int getRequestId() {
        return requestId;
    }

    @Override
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return toStringBuilder().append("RequestId", this.getRequestId()).toString();
    }
}
