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

package com.ibasco.agql.protocols.valve.source.query.utils;

import com.ibasco.agql.protocols.valve.source.query.SourceRconResponsePacket;
import org.apache.commons.lang3.RandomUtils;

/**
 * Source RCON Utility functions
 */
public class SourceRconUtil {
    /**
     * A reserved request id representing a special rcon terminator packet
     */
    public static final int RCON_TERMINATOR_RID = 8445800;

    /**
     * The minimum allowable value for an rcon request id
     */
    private static final int RCON_ID_MIN_RANGE = 100000000;

    /**
     * The maximum allowable value for an rcon request id
     */
    private static final int RCON_ID_MAX_RANGE = 999999999;

    /**
     * <p>Checks if the response packet is a valid rcon terminator packet</p>
     *
     * @param packet
     *         The {@link SourceRconResponsePacket} to check
     *
     * @return <code>true</code> if its a terminator packet
     */
    public static boolean isTerminator(SourceRconResponsePacket packet) {
        return isTerminator(packet.getId());
    }

    /**
     * <p>Checks if the rcon request id represents a terminator packet</p>
     *
     * @param requestId
     *         An integer representing an Rcon Request Id
     *
     * @return <code>true</code> if the given id represents a terminator
     */
    public static boolean isTerminator(int requestId) {
        return requestId == RCON_TERMINATOR_RID;
    }

    /**
     * <p>Checks if the request id is within the valid range</p>
     *
     * @param requestId
     *         An integer representing a request id
     *
     * @return <code>true</code> if the given request id is within the valid range
     */
    public static boolean isValidRequestId(int requestId) {
        return requestId >= RCON_ID_MIN_RANGE && requestId <= RCON_ID_MAX_RANGE;
    }

    /**
     * A utility method to generate random request ids
     *
     * @return An random integer ranging from 100000000 to 999999999
     */
    public static int createRequestId() {
        return RandomUtils.nextInt(RCON_ID_MIN_RANGE, RCON_ID_MAX_RANGE);
    }
}
