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

import com.ibasco.agql.core.AbstractPacket;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconRequestType;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by raffy on 9/24/2016.
 */
public abstract class SourceRconPacket extends AbstractPacket {
    private int size;
    private int id;
    private int type;
    private String body;

    public SourceRconPacket(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(SourceRconRequestType type) {
        this.type = type.getHeader();
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public ToStringBuilder toStringBuilder() {
        return super.toStringBuilder().append("id", getId())
                .append("rcon_packet_type", type);
    }
}
