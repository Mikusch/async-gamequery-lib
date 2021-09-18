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

package com.ibasco.agql.protocols.valve.source.query.pojos;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.net.InetSocketAddress;

public class SourceServer {
    private String name;
    private byte networkVersion;
    private String mapName;
    private String gameDirectory;
    private String gameDescription;
    private short appId;
    private byte numOfPlayers;
    private byte maxPlayers;
    private byte numOfBots;
    private boolean dedicated;
    private char operatingSystem;
    private boolean passwordProtected;
    private boolean secure;
    private String gameVersion;
    private long serverId;
    private short tvPort;
    private String tvName;
    private String serverTags;
    private long gameId;
    private InetSocketAddress address;

    public byte getNetworkVersion() {
        return networkVersion;
    }

    public void setNetworkVersion(byte networkVersion) {
        this.networkVersion = networkVersion;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getGameDirectory() {
        return gameDirectory;
    }

    public void setGameDirectory(String gameDirectory) {
        this.gameDirectory = gameDirectory;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public short getAppId() {
        return appId;
    }

    public void setAppId(short appId) {
        this.appId = appId;
    }

    public byte getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(byte numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public byte getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(byte maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public byte getNumOfBots() {
        return numOfBots;
    }

    public void setNumOfBots(byte numOfBots) {
        this.numOfBots = numOfBots;
    }

    public boolean isDedicated() {
        return dedicated;
    }

    public void setDedicated(boolean dedicated) {
        this.dedicated = dedicated;
    }

    public char getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(char operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public short getTvPort() {
        return tvPort;
    }

    public void setTvPort(short tvPort) {
        this.tvPort = tvPort;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public String getServerTags() {
        return serverTags;
    }

    public void setServerTags(String serverTags) {
        this.serverTags = serverTags;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public String getHostAddress() {
        return address.getAddress().getHostAddress();
    }

    public int getPort() {
        return address.getPort();
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE).append("name", this.getName())
                .append("map", getMapName())
                .append("description", gameDescription)
                .append("numOfPlayers", getNumOfPlayers())
                .append("maxPlayers", getMaxPlayers())
                .append("numOfBots", getNumOfBots())
                .append("operatingSystem", getOperatingSystem())
                .append("appId", getAppId())
                .append("isDedicated", isDedicated())
                .append("isSecure", isSecure())
                .append("serverId", getServerId())
                .append("tags", getServerTags())
                .toString();
    }
}
