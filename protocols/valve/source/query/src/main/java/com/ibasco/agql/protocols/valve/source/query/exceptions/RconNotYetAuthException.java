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

package com.ibasco.agql.protocols.valve.source.query.exceptions;

import com.ibasco.agql.core.exceptions.AsyncGameLibCheckedException;

/**
 * An {@link AsyncGameLibCheckedException} thrown during authentication errors
 */
public class RconNotYetAuthException extends AsyncGameLibCheckedException {
    public RconNotYetAuthException() {
    }

    public RconNotYetAuthException(String message) {
        super(message);
    }

    public RconNotYetAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public RconNotYetAuthException(Throwable cause) {
        super(cause);
    }

    public RconNotYetAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
