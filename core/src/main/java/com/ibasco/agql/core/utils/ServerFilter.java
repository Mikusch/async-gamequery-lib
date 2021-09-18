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

package com.ibasco.agql.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>Server Filter Utility class</p>
 * <br>
 * <h3>Example:</h3>
 * <code>
 * ServerFilter filter = ServerFilter.create().dedicated(true).appId(550);
 * </code>
 * <br >
 * <br >
 * <h3>Translates to:</h3>
 * <code>\dedicated\1\appId\550</code>
 * <br >
 * <br >
 *
 * @author Rafael Ibasco
 */
public final class ServerFilter {

    private static final String[] specialFilters = new String[] {"nand", "nor", "napp"};

    private final Map<FilterKey, Object> filterMap = new LinkedHashMap<>();

    protected static class FilterKey {

        private static int ctr;

        int id;

        String key;

        FilterKey(String key) {
            id = ctr++;
            this.key = key;
        }

        boolean isSpecial() {
            return Arrays.stream(specialFilters).anyMatch(p -> p.contains(key));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FilterKey that = (FilterKey) o;

            if (isSpecial())
                return key.equals(that.key) && (id == that.id);

            return key.equals(that.key);
        }

        @Override
        public int hashCode() {
            if (isSpecial())
                return Objects.hash(key, id);
            return Objects.hash(key);
        }

        @Override
        public String toString() {
            return key;
        }
    }

    /**
     * <p>A factory method to create a new {@link ServerFilter} instance</p>
     *
     * @return Instance of {@link ServerFilter}
     */
    public static ServerFilter create() {
        return new ServerFilter();
    }

    /**
     * A filter to return all available servers. Please note that applying this filter will override all previously defined filters of this instance.
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter allServers() {
        return create("allServers", null);
    }

    /**
     * <p>Servers that are spectator proxies</p>
     *
     * @param value
     *         Set to True to filter spectator proxy servers
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter isSpecProxy(Boolean value) {

        return create("proxy", value);
    }

    /**
     * <p>Servers that are not full</p>
     *
     * @param value
     *         Set to True to filter servers that are full
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter isFull(Boolean value) {

        return create("full", value);
    }

    /**
     * <p>Servers that are not empty</p>
     *
     * @param value
     *         Set to true to only filter servers that are empty
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter isEmpty(Boolean value) {

        return create("empty", value);
    }

    /**
     * <p>Filter for password protected servers</p>
     *
     * @param value
     *         Set to true to only filter servers that are password protected
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter isPasswordProtected(Boolean value) {

        return create("password", value);
    }

    /**
     * <p>Servers running on a Linux platform</p>
     *
     * @param value
     *         Set to true to filter servers only running under linux
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter isLinuxServer(Boolean value) {

        return create("linux", value);
    }

    /**
     * <p>Servers running the specified map (ex. cs_italy)</p>
     *
     * @param value
     *         Map name
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter mapName(String value) {
        return create("map", value);
    }

    /**
     * <p>Servers running the specified modification (ex. cstrike)</p>
     *
     * @param value
     *         The mode/game directory name (e.g. cstrike)
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter gamedir(String value) {
        return create("gamedir", value);
    }

    /**
     * <p>Servers using anti-cheat technology (VAC, but potentially others as well)</p>
     *
     * @param value
     *         Set to true to filter only secure servers (VAC protected)
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter isSecure(Boolean value) {

        return create("secure", value);
    }

    /**
     * <p>Servers running dedicated</p>
     *
     * @param value
     *         Set to true to filter only dedicated servers
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter dedicated(Boolean value) {

        return create("dedicated", value);
    }

    /**
     * <p>A special filter, specifies that servers matching all of the following [x] conditions should not be
     * returned</p>
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter nand() {
        return create("nand", "");
    }

    /**
     * <p>A special filter, specifies that servers matching any of the following [x] conditions should not be
     * returned</p>
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter nor() {
        return create("nor", "");
    }

    /**
     * <p>Servers that are NOT running game [appid] (This was introduced to block Left 4 Dead games from the Steam
     * Server Browser)</p>
     *
     * @param appId
     *         An integer representing the appId of a game
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter napp(Integer appId) {
        if (appId != null && appId > 0)
            return create("napp", appId);
        return this;
    }

    /**
     * <p>Servers that are empty</p>
     *
     * @param value
     *         Set to true to filter only empty servers
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter hasNoPlayers(Boolean value) {

        return create("noplayers", value);
    }

    /**
     * <p>Servers with all of the given tag(s) in sv_tags</p>
     *
     * @param tags
     *         A {@link String} array of tags
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter gametypes(String... tags) {
        return create("gametype", StringUtils.join(tags, ","));
    }

    /**
     * Servers with ALL of the given tag(s) in their 'hidden' tags (e.g. L4D2)
     *
     * @param tags
     *         Array of String game server tags
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter gamedata(String... tags) {
        return create("gamedata", StringUtils.join(tags, ","));
    }

    /**
     * Servers with ANY of the given tag(s) in their 'hidden' tags (e.g. L4D2)
     *
     * @param tags
     *         Array of String game server tags
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter gamedataOr(String... tags) {
        return create("gamedataor", StringUtils.join(tags, ","));
    }

    /**
     * Servers with their hostname matching [hostname]
     *
     * @param nameWildcard
     *         Hostname to lookup (can use * as a wildcard)
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter withHostName(String nameWildcard) {
        return create("name_match", nameWildcard);
    }

    /**
     * Servers running version [version]
     *
     * @param version
     *         Version to search (can use * as a wildcard)
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter hasVersion(String version) {
        return create("version_match", version);
    }

    /**
     * Return only one server for each unique IP address matched
     *
     * @param value
     *         Set to True to return only one server for each unique IP
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter onlyOneServerPerUniqueIp(Boolean value) {

        return create("collapse_addr_hash", value);
    }

    /**
     * <p>Return only servers on the specified IP address (port supported and optional)</p>
     *
     * @param ipPort
     *         IP[:port] format
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter hasServerIp(String ipPort) {
        return create("gameaddr", ipPort);
    }

    /**
     * <p>Servers that are whitelisted</p>
     *
     * @param value
     *         Set to true to filter only whitelisted servers
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter isWhitelisted(Boolean value) {

        return create("white", value);
    }

    /**
     * <p>Servers that are running game [appid]</p>
     *
     * @param appId
     *         An integer representing the appId of a game
     *
     * @return Instance of {@link ServerFilter}
     */
    public ServerFilter appId(Integer appId) {
        if (appId == null)
            return this;
        if (appId > 0)
            return create("appId", appId);
        return this;
    }

    /**
     * A utility method to create a Key-Value string pair
     *
     * @param key
     *         A {@link String} representing the key
     * @param value
     *         A {@link String} representing the value associated with the key
     *
     * @return Instance of {@link ServerFilter}
     */
    private ServerFilter create(String key, Object value) {
        if (key.equals("allServers")) {
            filterMap.put(new FilterKey(key), null);
            return this;
        } else if (StringUtils.isEmpty(key) || value == null) {
            return this;
        }

        Object tmpValue = value;

        if (tmpValue instanceof Boolean)
            tmpValue = ((Boolean) tmpValue) ? "1" : "0";

        if ("and".equals(key) || "or".equals(key) || "nor".equals(key)) {
            filterMap.put(new FilterKey(key), null);
        } else {
            filterMap.put(new FilterKey(key), tmpValue);
        }

        return this;
    }

    /**
     * Returns a {@link String} representation of this filter builder instance
     *
     * @return A {@link String}
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        //Do we have allServers set?
        if (filterMap.entrySet().stream().anyMatch(p -> p.getKey().key.equals("allServers"))) {
            return "";
        }

        for (Map.Entry<FilterKey, Object> entry : filterMap.entrySet()) {
            String key = entry.getKey().key;
            Object value = entry.getValue();

            if (value instanceof Boolean)
                value = ((Boolean) value) ? "1" : "0";

            if (entry.getKey().isSpecial()) {
                buffer.append("\\").append(key);
                if (value != null && !StringUtils.isBlank(value.toString())) {
                    buffer.append("\\").append(value);
                }
            } else {
                if (value == null)
                    value = "";
                buffer.append("\\").append(key).append("\\").append(value);
            }
        }
        return buffer.toString();
    }
}
