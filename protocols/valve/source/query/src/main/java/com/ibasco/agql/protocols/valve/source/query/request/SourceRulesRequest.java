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

package com.ibasco.agql.protocols.valve.source.query.request;

import com.ibasco.agql.protocols.valve.source.query.SourceServerRequest;
import com.ibasco.agql.protocols.valve.source.query.packets.request.SourceRulesRequestPacket;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/15/2016.
 */
public class SourceRulesRequest extends SourceServerRequest<SourceRulesRequestPacket> {
    private int challengeNumber;

    public SourceRulesRequest(int challenge, InetSocketAddress recipient) {
        super(recipient);
        this.challengeNumber = challenge;
    }

    @Override
    public SourceRulesRequestPacket getMessage() {
        return new SourceRulesRequestPacket(challengeNumber);
    }
}
