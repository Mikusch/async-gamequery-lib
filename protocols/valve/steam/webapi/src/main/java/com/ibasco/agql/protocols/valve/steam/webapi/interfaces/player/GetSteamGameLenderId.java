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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.player;

import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamPlayerServiceRequest;

/**
 * Returns valid lender SteamID if game currently played is borrowed
 */
public class GetSteamGameLenderId extends SteamPlayerServiceRequest {
    public GetSteamGameLenderId(int apiVersion, long steamId, int appId) {
        super("IsPlayingSharedGame", apiVersion);
        urlParam("steamid", steamId);
        urlParam("appid_playing", appId);
    }
}
