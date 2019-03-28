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

public class SteamGameServerAccount {

    @SerializedName("steamid")
    @Expose
    private String steamid;

    @SerializedName("appid")
    @Expose
    private Integer appid;

    @SerializedName("login_token")
    @Expose
    private String loginToken;

    @SerializedName("memo")
    @Expose
    private String memo;

    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;

    @SerializedName("is_expired")
    @Expose
    private Boolean isExpired;

    @SerializedName("rt_last_logon")
    @Expose
    private Integer rtLastLogon;

    public String getSteamid() {
        return steamid;
    }

    public void setSteamid(String steamid) {
        this.steamid = steamid;
    }

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }

    public Integer getRtLastLogon() {
        return rtLastLogon;
    }

    public void setRtLastLogon(Integer rtLastLogon) {
        this.rtLastLogon = rtLastLogon;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("steamid", steamid).append("appid", appid).append("loginToken", loginToken).append("memo", memo).append("isDeleted", isDeleted).append("isExpired", isExpired).append("rtLastLogon", rtLastLogon).toString();
    }
}
