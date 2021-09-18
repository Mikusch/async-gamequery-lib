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

package com.ibasco.agql.core.messenger;

import com.ibasco.agql.core.AbstractGameServerRequest;
import com.ibasco.agql.core.AbstractGameServerResponse;
import com.ibasco.agql.core.AbstractMessenger;
import com.ibasco.agql.core.enums.ProcessingMode;
import com.ibasco.agql.core.session.AbstractSessionIdFactory;

import java.util.concurrent.ExecutorService;

/**
 * Messenger using the UDP Transport protocol
 */
abstract public class GameServerMessenger<A extends AbstractGameServerRequest, B extends AbstractGameServerResponse>
        extends AbstractMessenger<A, B> {

    public GameServerMessenger(ProcessingMode processingMode) {
        super(processingMode);
    }

    public GameServerMessenger(ProcessingMode processingMode, ExecutorService executorService) {
        this(null, processingMode, executorService);
    }

    public GameServerMessenger(AbstractSessionIdFactory keyFactory, ProcessingMode processingMode) {
        this(keyFactory, processingMode, null);
    }

    public GameServerMessenger(AbstractSessionIdFactory keyFactory, ProcessingMode processingMode, ExecutorService executorService) {
        super(keyFactory, processingMode, executorService);
    }
}
