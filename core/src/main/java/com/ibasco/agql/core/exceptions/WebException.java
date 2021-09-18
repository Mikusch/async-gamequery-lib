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

import com.ibasco.agql.core.AbstractWebApiResponse;

public class WebException extends AsyncGameLibUncheckedException {

    private AbstractWebApiResponse response;

    public WebException(String message) {
        super(message);
    }

    public WebException(Throwable cause) {
        super(cause);
    }

    public WebException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebException(String message, AbstractWebApiResponse response) {
        super(formatErrMsg(message, response));
        this.response = response;
    }

    public WebException(Throwable cause, AbstractWebApiResponse response) {
        super(cause);
        this.response = response;
    }

    public AbstractWebApiResponse getResponse() {
        return response;
    }

    private static String formatErrMsg(String msg, AbstractWebApiResponse response) {
        if (response == null)
            return msg;
        return String.format("%s (Status Code: %d, Uri: %s)", msg, response.getMessage().getStatusCode(), response.getMessage().getUri().toUrl());
    }
}
