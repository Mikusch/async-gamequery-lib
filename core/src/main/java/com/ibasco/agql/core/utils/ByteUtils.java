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

package com.ibasco.agql.core.utils;

import org.apache.commons.codec.binary.Hex;

public class ByteUtils {
    /**
     * <p>Convert an integer to a primitive byte array type</p>
     *
     * @param integer
     *         The integer to be converted
     *
     * @return A byte array representation of the integer specified
     */
    public static byte[] byteArrayFromInteger(int integer) {
        return new byte[]{
                (byte) (integer >> 24),
                (byte) (integer >> 16),
                (byte) (integer >> 8),
                (byte) integer
        };
    }

    /**
     * <p>Convert an array of bytes to it's Hex-String representation</p>
     *
     * @param bytes
     *         The array of bytes to be converted
     *
     * @return A Hex {@link String} representation of the byte array
     */
    public static String bytesToHex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }
}
