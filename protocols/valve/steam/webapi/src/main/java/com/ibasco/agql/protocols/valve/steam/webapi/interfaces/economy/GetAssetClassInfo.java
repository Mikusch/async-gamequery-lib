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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.economy;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamEconomyRequest;

import java.util.List;

public class GetAssetClassInfo extends SteamEconomyRequest {
    public GetAssetClassInfo(int apiVersion, int appId, List<Long> classIds) {
        this(apiVersion, appId, null, classIds.toArray(new Long[0]));
    }

    public GetAssetClassInfo(int apiVersion, int appId, String language, Long... classIds) {
        super("GetAssetClassInfo", apiVersion);
        urlParam(SteamApiConstants.STEAM_URLPARAM_APPID, appId);
        //Add class id params
        if (classIds != null) {
            urlParam("class_count", classIds.length);
            urlParam("language", language);
            for (int i = 0; i < classIds.length; i++)
                urlParam(String.format("classid%d", i), classIds[i]);
        }
    }
}
