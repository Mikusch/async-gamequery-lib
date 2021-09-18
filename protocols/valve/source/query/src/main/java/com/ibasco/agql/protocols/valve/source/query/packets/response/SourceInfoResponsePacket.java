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
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import io.netty.buffer.ByteBuf;

/**
 * Created by raffy on 9/5/2016.
 */
public class SourceInfoResponsePacket extends SourceResponsePacket<SourceServer> {

    /**
     * This is for internal use only and no need to expose to the public API
     */
    private static byte EDF_GAME_ID = (byte) 0x01;
    private static byte EDF_GAME_PORT = (byte) 0x80;
    private static byte EDF_SERVER_ID = (byte) 0x10;
    private static byte EDF_SERVER_TAGS = (byte) 0x20;
    private static byte EDF_SOURCE_TV = (byte) 0x40;

    @Override
    public SourceServer toObject() {
        ByteBuf data = getPayloadBuffer();

        SourceServer server = new SourceServer();

        //Start Decoding
        server.setNetworkVersion(data.readByte());
        server.setName(readString(data));
        server.setMapName(readString(data));
        server.setGameDirectory(readString(data));
        server.setGameDescription(readString(data));
        server.setAppId(data.readShortLE());
        server.setNumOfPlayers(data.readByte());
        server.setMaxPlayers(data.readByte());
        server.setNumOfBots(data.readByte());
        server.setDedicated(data.readByte() == 1);
        server.setOperatingSystem((char) data.readByte());
        server.setPasswordProtected(data.readByte() == 1);
        server.setSecure(data.readByte() == 1);
        server.setGameVersion(readString(data));

        if (data.readableBytes() > 0) {
            byte extraDataFlag = data.readByte();

            if ((extraDataFlag & EDF_GAME_PORT) != 0) {
                data.readShortLE(); //discard, we already know which port based on the sender address
            }

            if ((extraDataFlag & EDF_SERVER_ID) != 0) {
                //this.info.put("serverId", Long.reverseBytes((this.contentData.getInt() << 32) | this.contentData.getInt()));
                server.setServerId(data.readLongLE());
            }

            if ((extraDataFlag & EDF_SOURCE_TV) != 0) {
                server.setTvPort(data.readShortLE());
                server.setTvName(readString(data));
            }

            if ((extraDataFlag & EDF_SERVER_TAGS) != 0) {
                server.setServerTags(readString(data));
            }

            if ((extraDataFlag & EDF_GAME_ID) != 0) {
                server.setGameId(data.readLongLE());
                //this.info.put("gameId", Long.reverseBytes((this.contentData.getInt() << 32) | this.contentData.getInt()));
            }
        }

        return server;
    }
}
