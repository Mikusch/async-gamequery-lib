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

import com.ibasco.agql.protocols.valve.source.query.enums.SourceGameResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceChallengeResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceInfoResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourcePlayerResponse;
import com.ibasco.agql.protocols.valve.source.query.response.SourceRulesResponse;

/**
 * Created by raffy on 9/19/2016.
 */
public class SourceResponseFactory {
    public static SourceServerResponse createResponseFrom(SourceResponsePacket packet) {
        SourceGameResponse type = SourceGameResponse.get(packet.getSingleBytePacketHeader());
        SourceServerResponse response;
        switch (type) {
            case CHALLENGE:
                response = new SourceChallengeResponse();
                break;
            case INFO:
                response = new SourceInfoResponse();
                break;
            case PLAYER:
                response = new SourcePlayerResponse();
                break;
            case RULES:
                response = new SourceRulesResponse();
                break;
            default:
                response = null;
                break;
        }
        return response;
    }
}
