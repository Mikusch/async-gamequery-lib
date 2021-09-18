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

package com.ibasco.agql.protocols.valve.source.query.packets.response;

import static com.ibasco.agql.core.utils.ByteBufUtils.readString;
import com.ibasco.agql.protocols.valve.source.query.SourceResponsePacket;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by raffy on 9/6/2016.
 */
public class SourceRulesResponsePacket extends SourceResponsePacket<Map<String, String>> {
    @Override
    public Map<String, String> toObject() {
        ByteBuf data = getPayloadBuffer();
        Map<String, String> ruleList = new HashMap<>();
        short numOfRules = data.readShortLE();
        for (int i = 0; i < numOfRules; i++) {
            String name = readString(data);
            String value = readString(data);
            ruleList.put(name, value);
        }
        return ruleList;
    }
}
