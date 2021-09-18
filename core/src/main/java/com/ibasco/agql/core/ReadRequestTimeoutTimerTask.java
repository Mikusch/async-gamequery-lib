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

package com.ibasco.agql.core;

import com.ibasco.agql.core.exceptions.ReadTimeoutException;
import com.ibasco.agql.core.session.SessionId;
import com.ibasco.agql.core.session.SessionManager;
import com.ibasco.agql.core.session.SessionValue;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class ReadRequestTimeoutTimerTask implements TimerTask {
    private static final Logger log = LoggerFactory.getLogger(ReadRequestTimeoutTimerTask.class);
    private SessionId id;
    private SessionManager sessionManager;

    public ReadRequestTimeoutTimerTask(SessionId sessionId, SessionManager sessionManager) {
        this.id = sessionId;
        this.sessionManager = sessionManager;
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        log.debug("Timeout occured for Session {}", id);
        //Notify the listener that timeout has occured
        final SessionValue session = sessionManager.getSession(id);

        //Do not proceed if the session is null
        if (session == null) {
            log.error("could not find session value for id {}. Registry Size : {}", id, sessionManager.getSessionEntries().size());
            return;
        }

        //Check first if the promise has been completed
        if (session.getClientPromise() != null && !session.getClientPromise().isDone() && !session.getClientPromise().isCancelled() && !timeout.isCancelled()) {
            //Send a ReadTimeoutException to the client
            session.getClientPromise().completeExceptionally(new ReadTimeoutException(id, String.format("Timeout occured for '%s' Started: %f seconds ago", id, ((double) Duration.ofMillis(System.currentTimeMillis() - session.getTimeRegistered()).toMillis() / 1000.0))));
        }
    }
}
