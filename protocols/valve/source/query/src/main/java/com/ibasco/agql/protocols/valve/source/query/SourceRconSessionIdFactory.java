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

import com.ibasco.agql.core.AbstractMessage;
import com.ibasco.agql.core.session.AbstractSessionIdFactory;
import com.ibasco.agql.core.session.SessionId;

/**
 * A factory class that generates a unique session id for each client request.
 *
 * Created by raffy on 9/26/2016.
 */
public class SourceRconSessionIdFactory extends AbstractSessionIdFactory<SourceRconRequest, SourceRconResponse> {
    @Override
    public SessionId createId(AbstractMessage message) {
        if (!(message instanceof SourceRconMessage)) {
            throw new IllegalStateException("Message is not an instance of SourceRconMessage");
        }
        /*String id = new StringBuffer().append(createIdStringFromMsg(message)).toString();*/
        String id = new StringBuffer().append(createIdStringFromMsg(message)).append(":")
                .append(((SourceRconMessage) message).getRequestId()).toString();
        return new SessionId(id);
    }

    @Override
    public SessionId duplicate(SessionId id) {
        return new SessionId(id);
    }
}
