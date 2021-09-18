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

import com.ibasco.agql.protocols.valve.source.query.enums.SourceGameRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Class Representing Source Server Queries
 */
public abstract class SourceRequestPacket extends SourceServerPacket {

    private static final Logger log = LoggerFactory.getLogger(SourceRequestPacket.class);

    @Override
    public void setHeader(byte header) {
        super.setHeader(new byte[]{header});
    }

    public void setHeader(SourceGameRequest header) {
        setHeader(header.getHeader());
    }
}
