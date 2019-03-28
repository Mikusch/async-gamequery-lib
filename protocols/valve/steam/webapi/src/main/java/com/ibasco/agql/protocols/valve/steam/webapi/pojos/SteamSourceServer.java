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
