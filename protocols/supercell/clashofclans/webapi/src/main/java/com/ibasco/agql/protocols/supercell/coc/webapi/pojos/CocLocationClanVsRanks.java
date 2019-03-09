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

package com.ibasco.agql.protocols.supercell.coc.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CocLocationClanVsRanks {
    @SerializedName("tag")
    private String tag;

    @SerializedName("name")
    private String name;

    @SerializedName("location")
    private CocLocationInfo location;

    @SerializedName("badgeUrls")
    private CocClanBadgeUrls badgeUrls;

    @SerializedName("clanLevel")
    private Integer clanLevel;

    @SerializedName("members")
    private Integer members;

    @SerializedName("rank")
    private Integer rank;

    @SerializedName("previousRank")
    private Integer previousRank;

    @SerializedName("clanVersusPoints")
    private Integer clanVersusPoints;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CocLocationInfo getLocation() {
        return location;
    }

    public void setLocation(CocLocationInfo location) {
        this.location = location;
    }

    public CocClanBadgeUrls getBadgeUrls() {
        return badgeUrls;
    }

    public void setBadgeUrls(CocClanBadgeUrls badgeUrls) {
        this.badgeUrls = badgeUrls;
    }

    public Integer getClanLevel() {
        return clanLevel;
    }

    public void setClanLevel(Integer clanLevel) {
        this.clanLevel = clanLevel;
    }

    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getPreviousRank() {
        return previousRank;
    }

    public void setPreviousRank(Integer previousRank) {
        this.previousRank = previousRank;
    }

    public Integer getClanVersusPoints() {
        return clanVersusPoints;
    }

    public void setClanVersusPoints(Integer clanVersusPoints) {
        this.clanVersusPoints = clanVersusPoints;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("tag", tag).append("name", name).append("location", location).append("badgeUrls", badgeUrls).append("clanLevel", clanLevel).append("members", members).append("rank", rank).append("previousRank", previousRank).append("clanVersusPoints", clanVersusPoints).toString();
    }
}
