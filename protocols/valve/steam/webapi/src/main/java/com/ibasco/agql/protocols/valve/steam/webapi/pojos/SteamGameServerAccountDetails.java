/*
 * MIT License
 *
 * Copyright (c) 2019 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class SteamGameServerAccountDetails {

    @SerializedName("servers")
    @Expose
    private List<SteamGameServerAccount> servers = new ArrayList<>();

    @SerializedName("is_banned")
    @Expose
    private Boolean isBanned;

    @SerializedName("expires")
    @Expose
    private Integer expires;

    @SerializedName("actor")
    @Expose
    private String actor;

    @SerializedName("last_action_time")
    @Expose
    private Integer lastActionTime;

    public List<SteamGameServerAccount> getServers() {
        return servers;
    }

    public void setServers(List<SteamGameServerAccount> servers) {
        this.servers = servers;
    }

    public Boolean getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(Boolean isBanned) {
        this.isBanned = isBanned;
    }

    public Integer getExpires() {
        return expires;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public Integer getLastActionTime() {
        return lastActionTime;
    }

    public void setLastActionTime(Integer lastActionTime) {
        this.lastActionTime = lastActionTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("servers", servers).append("isBanned", isBanned).append("expires", expires).append("actor", actor).append("lastActionTime", lastActionTime).toString();
    }
}
