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

import com.ibasco.agql.core.AbstractGameServerRequest;

import java.net.InetSocketAddress;

public abstract class SourceRconRequest<T extends SourceRconRequestPacket>
        extends AbstractGameServerRequest<T>
        implements SourceRconMessage {
    private int requestId;

    public SourceRconRequest(InetSocketAddress recipient, int requestId) {
        super(recipient);
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
