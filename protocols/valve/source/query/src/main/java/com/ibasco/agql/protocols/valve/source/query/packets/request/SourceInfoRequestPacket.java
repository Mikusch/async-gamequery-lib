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

package com.ibasco.agql.protocols.valve.source.query.packets.request;

import com.ibasco.agql.protocols.valve.source.query.SourceRequestPacket;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceGameRequest;

/**
 * Created by raffy on 9/1/2016.
 */
public class SourceInfoRequestPacket extends SourceRequestPacket {

    public SourceInfoRequestPacket() {
        setHeader(SourceGameRequest.INFO);
        setPayload("Source Engine Query\0".getBytes());
    }
}
