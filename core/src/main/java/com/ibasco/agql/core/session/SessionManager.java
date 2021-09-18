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

package com.ibasco.agql.core.session;

import com.ibasco.agql.core.AbstractMessage;
import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.RequestDetails;

import java.io.Closeable;
import java.util.Collection;
import java.util.Map;

public interface SessionManager<R extends AbstractRequest, S extends AbstractResponse> extends Closeable {
    /**
     * Returns the {@link SessionValue} based on the {@link SessionId} provided.
     * If multiple values exists for the same key, only the head {@link SessionValue} element will be retrieved.
     * This is somehow similar to peek() in Queue based implementations. The ordering is guaranteed by the {@link SessionValue} index.
     *
     * @param id The {@link SessionId} instance to be used as the lookup key.
     *
     * @return A {@link SessionValue} or NULL if no session found for the specified id
     */
    SessionValue<R, S> getSession(SessionId id);

    /**
     * Get session based on the {@link AbstractMessage} instance
     *
     * @param message The {@link AbstractMessage} instance to use for the lookup reference
     *
     * @return A {@link SessionValue} instance if found. Null if a session does not exists for the specified {@link AbstractMessage}
     */
    SessionValue<R, S> getSession(AbstractMessage message);

    /**
     * Returns the session id from the registry.
     *
     * @param message An instance of {@link AbstractMessage} to use as a reference for the {@link SessionId} lookup.
     *
     * @return The {@link SessionId} instance retrieved from the registry. Null if no {@link SessionId} has been found in the registry for the message.
     */
    SessionId getId(AbstractMessage message);

    /**
     * Registers the request to the session registry. This should be used once a request has been delivered via the underlying transport.
     * If the same request type already exists in the registry, it will not replace the existing, rather it will be placed within the same collection under the same {@link SessionId}.
     *
     * @param requestDetails A {@link RequestDetails} instance containing the details of the request.
     *
     * @return A {@link SessionId} if the request has been successfully registered.
     */
    SessionId create(RequestDetails<R, S> requestDetails);

    /**
     * Removes the associated session from the registry (if available) using a {@link SessionId} instance.
     * This will try to retrieve the existing {@link SessionValue} from the registry using the id provided.
     * If multiple sessions exists for this id, then only the top {@link SessionValue} will be removed.
     *
     * @param id The {@link SessionId} instance to be used as the lookup reference for the session.
     *
     * @return <code>true</code> if the operation has been successful.
     */
    boolean delete(SessionId id);

    /**
     * Removes the associated session from the registry (if available) using the {@link SessionValue} provided.
     *
     * @param value The {@link SessionValue} instance to be used as the lookup reference for the session.
     *
     * @return <code>true</code> if the operation has been successful.
     */
    boolean delete(SessionValue value);

    /**
     * This lookups the session registry map and indicates whether or not the message is in the registry or not
     *
     * @param message An instance of {@link AbstractMessage} to be used as the lookup reference in the registry.
     *
     * @return <code>true</code> if the specified message is registered in the session registry otherwise false.
     */
    boolean isRegistered(AbstractMessage message);

    /**
     * <p>Returns a flat representation of the existing entries in the session registry</p>
     *
     * @return a collection containing entries of {@link SessionId} and {@link SessionValue} pair.
     */
    Collection<Map.Entry<SessionId, SessionValue<R, S>>> getSessionEntries();

    Class<? extends S> findResponseClass(R request);

    Map<Class<? extends R>, Class<? extends S>> getLookupMap();

    SessionIdFactory getSessionIdFactory();
}
