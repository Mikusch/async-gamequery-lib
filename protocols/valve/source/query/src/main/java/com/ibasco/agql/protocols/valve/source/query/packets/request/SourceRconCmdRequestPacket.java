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
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by raffy on 9/24/2016.
 */
public class SourceRconCmdRequestPacket extends SourceRconRequestPacket {

    private String command;

    public SourceRconCmdRequestPacket(int id, String command) {
        super(id);
        setType(SourceRconRequestType.COMMAND);
        setBody(command);
        this.command = command;
    }

    @Override
    public ToStringBuilder toStringBuilder() {
        return super.toStringBuilder().append("rcon_command", command);
    }
}
