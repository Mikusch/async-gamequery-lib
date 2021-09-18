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

public class SteamSourceServer {

    @SerializedName("addr")
    @Expose
    private String addr;

    @SerializedName("gameport")
    @Expose
    private Integer gameport;

    @SerializedName("steamid")
    @Expose
    private Long steamid;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("appid")
    @Expose
    private Integer appid;

    @SerializedName("gamedir")
    @Expose
    private String gamedir;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("product")
    @Expose
    private String product;

    @SerializedName("region")
    @Expose
    private Integer region;

    @SerializedName("players")
    @Expose
    private Integer players;

    @SerializedName("max_players")
    @Expose
    private Integer maxPlayers;

    @SerializedName("bots")
    @Expose
    private Integer bots;

    @SerializedName("map")
    @Expose
    private String map;

    @SerializedName("secure")
    @Expose
    private Boolean secure;

    @SerializedName("dedicated")
    @Expose
    private Boolean dedicated;

    @SerializedName("os")
    @Expose
    private String os;

    @SerializedName("gametype")
    @Expose
    private String gametype;

    @SerializedName("specport")
    @Expose
    private Integer specport;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getGameport() {
        return gameport;
    }

    public void setGameport(Integer gameport) {
        this.gameport = gameport;
    }

    public Long getSteamid() {
        return steamid;
    }

    public void setSteamid(Long steamid) {
        this.steamid = steamid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    public String getGamedir() {
        return gamedir;
    }

    public void setGamedir(String gamedir) {
        this.gamedir = gamedir;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getPlayers() {
        return players;
    }

    public void setPlayers(Integer players) {
        this.players = players;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Integer getBots() {
        return bots;
    }

    public void setBots(Integer bots) {
        this.bots = bots;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public Boolean getDedicated() {
        return dedicated;
    }

    public void setDedicated(Boolean dedicated) {
        this.dedicated = dedicated;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getGametype() {
        return gametype;
    }

    public void setGametype(String gametype) {
        this.gametype = gametype;
    }

    public Integer getSpecport() {
        return specport;
    }

    public void setSpecport(Integer specport) {
        this.specport = specport;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("addr", addr).append("gameport", gameport).append("steamid", steamid).append("name", name).append("appid", appid).append("gamedir", gamedir).append("version", version).append("product", product).append("region", region).append("players", players).append("maxPlayers", maxPlayers).append("bots", bots).append("map", map).append("secure", secure).append("dedicated", dedicated).append("os", os).append("gametype", gametype).append("specport", specport).toString();
    }

}
