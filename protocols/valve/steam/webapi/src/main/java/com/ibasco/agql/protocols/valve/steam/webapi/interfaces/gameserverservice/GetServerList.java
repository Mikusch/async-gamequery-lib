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

import com.ibasco.agql.core.utils.ServerFilter;
import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamGameServerRequest;

public class GetServerList extends SteamGameServerRequest {

    public GetServerList(int apiVersion, ServerFilter filter) {
        this(apiVersion, filter, 5000);
    }

    public GetServerList(int apiVersion, ServerFilter filter, int limit) {
        super("GetServerList", apiVersion);
        urlParam("filter", filter.toString());
        urlParam("limit", limit);
    }
}
