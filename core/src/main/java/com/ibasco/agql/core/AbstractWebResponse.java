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

import io.netty.handler.codec.http.HttpStatusClass;
import org.asynchttpclient.Response;

import java.net.InetSocketAddress;

abstract public class AbstractWebResponse extends AbstractResponse<Response> {

    private Response response;

    public AbstractWebResponse(Response response) {
        super((InetSocketAddress) response.getRemoteAddress(), (InetSocketAddress) response.getLocalAddress());
        this.response = response;
    }

    public HttpStatusClass getStatus() {
        return HttpStatusClass.valueOf(this.response.getStatusCode());
    }

    @Override
    public Response getMessage() {
        return this.response;
    }
}
