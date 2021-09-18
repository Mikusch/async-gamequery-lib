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

import com.ibasco.agql.protocols.valve.source.query.SourceRconRequestPacket;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconRequestType;
import com.ibasco.agql.protocols.valve.source.query.utils.SourceRconUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * This packet is sent right after each command request.
 * </p>
 *
 * @see <a href="https://developer.valvesoftware.com/wiki/Talk:Source_RCON_Protocol">How
 * to handle split packet response</a>
 */
public class SourceRconTermRequestPacket extends SourceRconRequestPacket {

    public SourceRconTermRequestPacket() {
        super(SourceRconUtil.RCON_TERMINATOR_RID);
        setSize(0);
        setType(SourceRconRequestType.RESPONSE);
        setBody(StringUtils.EMPTY);
    }
}
