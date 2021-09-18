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
 * Created by raffy on 9/24/2016.
 */
public enum SourceRconRequestType {
    AUTH((byte) 3),
    COMMAND((byte) 2),
    RESPONSE((byte) 0); //used for special circumstances

    private byte header;

    SourceRconRequestType(byte header) {
        this.header = header;
    }

    public static SourceRconRequestType get(byte header) {
        return Arrays.stream(values()).filter(req -> req.getHeader() == header).findFirst().orElse(null);
    }

    public byte getHeader() {
        return header;
    }
}
