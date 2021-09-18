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
public enum SourceRconResponseType {
    AUTH(2),
    COMMAND(0),
    TERMINATOR(-1);

    private int header;

    SourceRconResponseType(int header) {
        this.header = header;
    }

    public static SourceRconResponseType get(int header) {
        return Arrays.stream(values()).filter(res -> res.getHeader() == header).findFirst().orElse(null);
    }

    public static boolean isValid(int header) {
        return get(header) != null;
    }

    public int getHeader() {
        return header;
    }
}
