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

package com.ibasco.agql.protocols.valve.source.query.enums;

import java.util.Arrays;

/**
 * Created by raffy on 9/11/2016.
 */
public enum SourceGameResponse {

    CHALLENGE((byte) 0x41),
    INFO((byte) 0x49),
    MASTER((byte) 0x66),
    MASTER_SECONDARY((byte) 0x0A),
    PLAYER((byte) 0x44),
    RULES((byte) 0x45);

    private byte header;

    SourceGameResponse(byte header) {
        this.header = header;
    }

    public static SourceGameResponse get(byte header) {
        return Arrays.stream(values()).filter(sourceResponse -> sourceResponse.getHeader() == header).findFirst().orElse(null);
    }

    public byte getHeader() {
        return header;
    }
}
