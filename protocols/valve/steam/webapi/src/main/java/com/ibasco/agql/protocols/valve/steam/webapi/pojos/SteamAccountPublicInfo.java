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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SteamAccountPublicInfo {

    @SerializedName("steamid")
    @Expose
    private Long steamid;

    @SerializedName("appid")
    @Expose
    private Integer appid;

    public Long getSteamid() {
        return steamid;
    }

    public void setSteamid(Long steamid) {
        this.steamid = steamid;
    }

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("steamid", steamid).append("appid", appid).toString();
    }

}