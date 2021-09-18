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

import com.ibasco.agql.core.utils.ByteBufUtils;
import com.ibasco.agql.protocols.valve.source.query.SourceResponsePacket;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourcePlayer;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raffy on 9/6/2016.
 */
public class SourcePlayerResponsePacket extends SourceResponsePacket<List<SourcePlayer>> {
    @Override
    public List<SourcePlayer> toObject() {
        ByteBuf data = getPayloadBuffer();
        List<SourcePlayer> playerList = new ArrayList<>();
        byte numOfPlayers = data.readByte();
        for (int i = 0; i < numOfPlayers; i++)
            playerList.add(new SourcePlayer(data.readByte(), ByteBufUtils.readString(data, CharsetUtil.UTF_8), data.readIntLE(), Float.intBitsToFloat(data.readIntLE())));
        return playerList;
    }
}
