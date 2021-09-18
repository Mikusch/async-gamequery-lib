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

package com.ibasco.agql.core.client;

import com.ibasco.agql.core.AbstractClient;
import com.ibasco.agql.core.AbstractGameServerRequest;
import com.ibasco.agql.core.AbstractGameServerResponse;
import com.ibasco.agql.core.messenger.GameServerMessenger;

abstract public class AbstractGameServerClient<Req extends AbstractGameServerRequest,
        Res extends AbstractGameServerResponse,
        M extends GameServerMessenger<Req, Res>>
        extends AbstractClient<Req, Res, M> {
    public AbstractGameServerClient(M messenger) {
        super(messenger);
    }
}
