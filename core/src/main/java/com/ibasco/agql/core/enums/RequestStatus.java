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

package com.ibasco.agql.core.enums;

/**
 * Identifies the stages of the request flow
 */
public enum RequestStatus {
    /**
     * Unprocessed request and is currently in-queue
     */
    NEW,
    /**
     * Request has been removed from the queue and is currently being processed
     */
    ACCEPTED,
    /**
     * Request could not be sent at this time, if marked for RETRY,
     * it will be placed on the bottom of the queue and scheduled for re-processing.
     */
    RETRY,
    /**
     * Indicates that the request is now in preparation stage and will be sent through the transport
     */
    REGISTERED,
    /**
     * A request has been issued to the underlying transport but is currently awaiting for completion
     */
    AWAIT,
    /**
     * Request has been sent through the underlying transport and is awaiting for response
     */
    SENT,
    /**
     * Response from the server has been received
     */
    DONE
}
