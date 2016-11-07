/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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

package org.ribasco.agql.protocols.supercell.coc.webapi.interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.ribasco.agql.protocols.supercell.coc.webapi.CocWebApiClient;
import org.ribasco.agql.protocols.supercell.coc.webapi.CocWebApiInterface;
import org.ribasco.agql.protocols.supercell.coc.webapi.interfaces.locations.GetClanRankingsForLoc;
import org.ribasco.agql.protocols.supercell.coc.webapi.interfaces.locations.GetLocationInfo;
import org.ribasco.agql.protocols.supercell.coc.webapi.interfaces.locations.GetLocations;
import org.ribasco.agql.protocols.supercell.coc.webapi.interfaces.locations.GetPlayerRankingsForLoc;
import org.ribasco.agql.protocols.supercell.coc.webapi.pojos.CocClanRankInfo;
import org.ribasco.agql.protocols.supercell.coc.webapi.pojos.CocLocation;
import org.ribasco.agql.protocols.supercell.coc.webapi.pojos.CocPlayerRankInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p>A Web API Implementation of the Location interface. Contains methods for clan/player inquiries based on location.</p>
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://developer.clashofclans.com/api-docs/index.html#!/locations">Clash of Clans API - Locations</a>
 */
public class CocLocations extends CocWebApiInterface {
    /**
     * <p>Default Constructor</p>
     *
     * @param client A {@link CocWebApiClient} instance
     */
    public CocLocations(CocWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<CocLocation>> getLocations() {
        return getLocations(-1, -1, -1);
    }

    public CompletableFuture<List<CocLocation>> getLocations(int limit) {
        return getLocations(limit, -1, -1);
    }

    public CompletableFuture<List<CocLocation>> getLocations(int limit, int before, int after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLocations(VERSION_1, limit, before, after));
        return json.thenApply(new Function<JsonObject, List<CocLocation>>() {
            @Override
            public List<CocLocation> apply(JsonObject root) {
                JsonArray items = root.getAsJsonArray("items");
                return builder().fromJson(items, new TypeToken<List<CocLocation>>() {
                }.getType());
            }
        });
    }

    public CompletableFuture<CocLocation> getLocationInfo(int locationId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLocationInfo(VERSION_1, locationId));
        return json.thenApply(root -> builder().fromJson(root, CocLocation.class));
    }

    public CompletableFuture<List<CocClanRankInfo>> getClanRankingsFromLocation(int locationId) {
        return getClanRankingsFromLocation(locationId, -1);
    }

    public CompletableFuture<List<CocClanRankInfo>> getClanRankingsFromLocation(int locationId, int limit) {
        return getClanRankingsFromLocation(locationId, limit, -1, -1);
    }

    public CompletableFuture<List<CocClanRankInfo>> getClanRankingsFromLocation(int locationId, int limit, int before, int after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetClanRankingsForLoc(VERSION_1, locationId, limit, before, after));
        return json.thenApply(new Function<JsonObject, List<CocClanRankInfo>>() {
            @Override
            public List<CocClanRankInfo> apply(JsonObject root) {
                JsonArray items = root.getAsJsonArray("items");
                return builder().fromJson(items, new TypeToken<List<CocClanRankInfo>>() {
                }.getType());
            }
        });
    }

    public CompletableFuture<List<CocPlayerRankInfo>> getPlayerRankingsFromLocation(int locationId) {
        return getPlayerRankingsFromLocation(locationId, -1);
    }

    public CompletableFuture<List<CocPlayerRankInfo>> getPlayerRankingsFromLocation(int locationId, int limit) {
        return getPlayerRankingsFromLocation(locationId, limit, -1, -1);
    }

    public CompletableFuture<List<CocPlayerRankInfo>> getPlayerRankingsFromLocation(int locationId, int limit, int before, int after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerRankingsForLoc(VERSION_1, locationId, limit, before, after));
        return json.thenApply(new Function<JsonObject, List<CocPlayerRankInfo>>() {
            @Override
            public List<CocPlayerRankInfo> apply(JsonObject root) {
                JsonArray items = root.getAsJsonArray("items");
                return builder().fromJson(items, new TypeToken<List<CocPlayerRankInfo>>() {
                }.getType());
            }
        });
    }

}
