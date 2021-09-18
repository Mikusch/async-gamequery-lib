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

import java.net.InetSocketAddress;
import java.util.Map;

abstract public class AbstractSessionIdFactory<
        Req extends AbstractRequest,
        Res extends AbstractResponse>
        implements SessionIdFactory {

    protected static final String DEFAULT_ID_FORMAT = "%s:%s:%s";

    private Map<Class<? extends Req>, Class<? extends Res>> lookup;

    protected Class<? extends Res> getResponseClass(Req request) {
        if (lookup == null)
            throw new IllegalStateException("Lookup map has not been initialized");
        if (request != null)
            return lookup.get(request.getClass());
        return null;
    }

    /**
     * <p>Create a generic id based on the message</p>
     *
     * @param message The {@link AbstractMessage} instance to be converted to an Id
     *
     * @return A {@link String} id representation of the {@link AbstractMessage} provided
     */
    protected String createIdStringFromMsg(AbstractMessage message) {
        //Format: <response class name> : <ip address> : <port>
        if (message == null)
            throw new IllegalArgumentException("Message not specified");

        InetSocketAddress address;
        Class messageClass;

        if (message instanceof AbstractRequest) {
            messageClass = getResponseClass((Req) message);
            address = message.recipient();
        } else {
            messageClass = message.getClass();
            address = message.sender();
        }

        if (address == null) {
            throw new IllegalStateException("Unable to resolve address for message: " + message);
        }

        return String.format(DEFAULT_ID_FORMAT,
                messageClass.getSimpleName(),
                address.getAddress().getHostAddress(),
                String.valueOf(address.getPort()));
    }

    public Map<Class<? extends Req>, Class<? extends Res>> getLookup() {
        return lookup;
    }

    public void setLookup(Map<Class<? extends Req>, Class<? extends Res>> lookup) {
        this.lookup = lookup;
    }
}
