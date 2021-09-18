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

package com.ibasco.agql.protocols.valve.steam.webapi;

import com.ibasco.agql.core.client.AbstractRestClient;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class SteamWebApiClient extends AbstractRestClient<SteamWebApiRequest, SteamWebApiResponse> {

    private static final Logger log = LoggerFactory.getLogger(SteamWebApiClient.class);

    /**
     * Some requests do not require a token
     */
    public SteamWebApiClient() {
        this(null);
    }

    public SteamWebApiClient(String apiToken) {
        this(apiToken, null);
    }

    public SteamWebApiClient(String apiToken, ExecutorService executorService) {
        super(apiToken, executorService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SteamWebApiResponse createWebApiResponse(Response response) {
        log.debug("Creating Response for : {}", response);
        return new SteamWebApiResponse(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void applyAuthenticationScheme(RequestBuilder requestBuilder, String authToken) {
        requestBuilder.addQueryParam("key", authToken);
    }
}
