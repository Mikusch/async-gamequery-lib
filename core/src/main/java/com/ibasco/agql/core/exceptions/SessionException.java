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

package com.ibasco.agql.core.exceptions;

import com.ibasco.agql.core.session.SessionId;

public class SessionException extends AsyncGameLibCheckedException {

    private SessionId sessionId;

    public SessionException(SessionId sessionId) {
        this(sessionId, "");
    }

    public SessionException(SessionId sessionId, String message) {
        this(sessionId, message, null);
    }

    public SessionException(SessionId sessionId, Throwable cause) {
        this(sessionId, null, cause);
    }

    public SessionException(SessionId sessionId, String message, Throwable cause) {
        this(sessionId, message, cause, false, false);
    }

    public SessionException(SessionId sessionId, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.sessionId = sessionId;
    }

    public SessionId getSessionId() {
        return sessionId;
    }

    public void setSessionId(SessionId sessionId) {
        this.sessionId = sessionId;
    }
}
