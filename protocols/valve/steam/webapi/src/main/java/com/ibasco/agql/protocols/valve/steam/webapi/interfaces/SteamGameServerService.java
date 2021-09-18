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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibasco.agql.core.utils.ServerFilter;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.gameserverservice.*;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SteamGameServerService extends SteamWebApiInterface {

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link SteamWebApiClient} instance
     */
    public SteamGameServerService(SteamWebApiClient client) {
        super(client);
    }

    /**
     * Get server list
     *
     * @param filter
     *         The server filter
     * @param limit
     *         Limit for the result
     *
     * @return A list of {@link SteamSourceServer}
     */
    public CompletableFuture<List<SteamSourceServer>> getServerList(ServerFilter filter, int limit) {
        CompletableFuture<JsonObject> res = sendRequest(new GetServerList(VERSION_1, filter, limit));
        return res.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("response");
            if (result.has("servers")) {
                return fromJson(result.getAsJsonArray("servers"), new TypeToken<List<SteamSourceServer>>() {
                }.getType());
            }
            return new ArrayList<>();
        });
    }

    /**
     * Gets a list of game server accounts with their logon tokens
     *
     * @return Details of the game server accounts associated with the specified key
     */
    public CompletableFuture<SteamGameServerAccountDetails> getAccountList() {
        return getAccountList(null);
    }

    /**
     * Gets a list of game server accounts with their logon tokens
     *
     * @param key
     *         The steam api key of the owner
     *
     * @return Details of the game server accounts associated with the specified key
     */
    public CompletableFuture<SteamGameServerAccountDetails> getAccountList(String key) {
        CompletableFuture<JsonObject> res = sendRequest(new GetAccountList(VERSION_1, key));
        return res.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("response");
            if (result == null)
                return null;
            return fromJson(result, SteamGameServerAccountDetails.class);
        });
    }

    /**
     * Gets public information about a given game server account
     *
     * @param steamId
     *         The steamm id of the server to query
     *
     * @return The public account information
     */
    public CompletableFuture<SteamAccountPublicInfo> getAccountPublicInfo(long steamId) {
        CompletableFuture<JsonObject> res = sendRequest(new GetAccountPublicInfo(VERSION_1, steamId));
        return res.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("response");
            if (result == null)
                return null;
            return fromJson(result, SteamAccountPublicInfo.class);
        });
    }

    /**
     * Queries the status of the specified token, which must be owned by you
     *
     * @param loginToken
     *         Login token to query
     *
     * @return The {@link SteamLoginTokenStatus} containing the details of the token
     */
    public CompletableFuture<SteamLoginTokenStatus> queryLoginToken(String loginToken) {
        CompletableFuture<JsonObject> res = sendRequest(new QueryLoginToken(VERSION_1, loginToken));
        return res.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("response");
            if (result == null)
                return null;
            return fromJson(result, SteamLoginTokenStatus.class);
        });
    }

    /**
     * Creates a persistent game server account
     *
     * @param appId
     *         The app id of the game server
     * @param memo
     *         The memo to set on the new account
     *
     * @return The server response
     */
    public CompletableFuture<SteamCreateAccountResponse> createAccount(int appId, String memo) {
        return createAccount(appId, memo, null);
    }

    /**
     * Creates a persistent game server account
     *
     * @param appId
     *         The app id of the game server
     * @param memo
     *         The memo to set on the new account
     * @param key
     *         The steam api key of the owner
     *
     * @return The server response
     */
    public CompletableFuture<SteamCreateAccountResponse> createAccount(int appId, String memo, String key) {
        CompletableFuture<JsonObject> res = sendRequest(new CreateAccount(VERSION_1, appId, memo, key));
        return res.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("response");
            if (result == null)
                return null;
            return fromJson(result, SteamCreateAccountResponse.class);
        });
    }

    /**
     * Deletes a persistent game server account
     *
     * @param steamId
     *         The steamid of the server to delete
     *
     * @return <code>true</code> if the operation was successful (no exceptions thrown)
     */
    public CompletableFuture<Boolean> deleteAccount(long steamId) {
        return deleteAccount(steamId, null);
    }

    /**
     * Deletes a persistent game server account
     *
     * @param steamId
     *         The steamid of the server to delete
     * @param key
     *         The steam api key of the owner
     *
     * @return <code>true</code> if the operation was successful
     */
    public CompletableFuture<Boolean> deleteAccount(long steamId, String key) {
        CompletableFuture<JsonObject> res = sendRequest(new DeleteAccount(VERSION_1, steamId, key));
        return res.thenApply(root -> {
            if (root.has("response"))
                return true;
            return false;
        });
    }

    /**
     * Generates a new login token for the specified game server
     *
     * @param steamId
     *         The steam id of the server to reset
     *
     * @return The new token
     */
    public CompletableFuture<SteamLoginToken> resetLoginToken(long steamId) {
        return resetLoginToken(steamId, null);
    }

    /**
     * Generates a new login token for the specified game server
     *
     * @param steamId
     *         The steam id of the server to reset
     * @param key
     *         The steam api key of the owner
     *
     * @return The new token
     */
    public CompletableFuture<SteamLoginToken> resetLoginToken(long steamId, String key) {
        CompletableFuture<JsonObject> res = sendRequest(new ResetLoginToken(VERSION_1, steamId, key));
        return res.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("response");
            if (result == null)
                return null;
            return fromJson(result, SteamLoginToken.class);
        });
    }

    /**
     * This method changes the memo associated with the game server account. Memos do not affect the account in any way. The memo shows up in the GetAccountList response and serves only as a reminder of what the account is used for.
     *
     * @param steamId
     *         The SteamID of the game server to set the memo on
     * @param memo
     *         The memo to set on the new account
     *
     * @return true if the operation was successful
     */
    public CompletableFuture<Boolean> setMemo(long steamId, String memo) {
        return setMemo(steamId, memo, null);
    }

    /**
     * This method changes the memo associated with the game server account. Memos do not affect the account in any way. The memo shows up in the GetAccountList response and serves only as a reminder of what the account is used for.
     *
     * @param steamId
     *         The SteamID of the game server to set the memo on
     * @param memo
     *         The memo to set on the new account
     * @param key
     *         The steam api key of the owner
     *
     * @return true if the operation was successful
     */
    public CompletableFuture<Boolean> setMemo(long steamId, String memo, String key) {
        CompletableFuture<JsonObject> res = sendRequest(new SetMemo(VERSION_1, steamId, memo, key));
        return res.thenApply(root -> {
            if (root.has("response"))
                return true;
            return false;
        });
    }

    //TODO: no sample data available: https://api.steampowered.com/IGameServersService/GetServerIPsBySteamID/v1/?key=903BC0B13739EF74242523BC3013F076&server_steamids=85568392922810043
    public CompletableFuture<Void> getServerIPsBySteamID() {
        return null;
    }

    //TODO: no sample data avilable: https://api.steampowered.com/IGameServersService/GetServerSteamIDsByIP/v1/?key=903BC0B13739EF74242523BC3013F076&server_ips=45.121.185.47
    public CompletableFuture<Void> getServerSteamIDsByIP() {
        return null;
    }
}
