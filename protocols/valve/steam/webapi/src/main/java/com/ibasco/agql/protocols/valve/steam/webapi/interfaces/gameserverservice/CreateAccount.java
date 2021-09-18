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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.gameserverservice;

import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamGameServerRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.apache.commons.lang3.StringUtils;

public class CreateAccount extends SteamGameServerRequest {

    public CreateAccount(int apiVersion, int appId, String memo, String key) {
        super("CreateAccount", apiVersion);
        method(HttpMethod.POST);
        urlParam("appid", appId);
        urlParam("memo", memo);
        if (!StringUtils.isBlank(key))
            urlParam("key", key);
    }
}
