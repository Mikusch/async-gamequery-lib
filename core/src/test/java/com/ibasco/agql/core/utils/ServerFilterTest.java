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

package com.ibasco.agql.core.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

class ServerFilterTest {

    private static final Logger log = LoggerFactory.getLogger(ServerFilterTest.class);

    @Test
    @DisplayName("Test filter keys inside a map")
    void testFilterKeysInsideMap() {
        Map<ServerFilter.FilterKey, Object> filterMap = new HashMap<>();
        filterMap.put(new ServerFilter.FilterKey("appId"), 550);
        filterMap.put(new ServerFilter.FilterKey("napp"), null);
        filterMap.put(new ServerFilter.FilterKey("napp"), null);
        assertEquals(3, filterMap.size());
        assertTrue(filterMap.containsKey(new ServerFilter.FilterKey("appId")));
    }

    @Test
    @DisplayName("Test equality for special keys")
    void testEqualityForSpecialKeys() {
        ServerFilter.FilterKey key1 = new ServerFilter.FilterKey("nand");
        ServerFilter.FilterKey key2 = new ServerFilter.FilterKey("nand");

        assertNotEquals(key1, key2);
        assertNotEquals(key1.id, key2.id);
        assertEquals(key1.key, key2.key);
        assertNotEquals(key1.hashCode(), key2.hashCode());
        assertNotEquals(key1, key2);
        assertTrue(key1.isSpecial());
        assertTrue(key2.isSpecial());
    }

    @Test
    @DisplayName("Test equality for non-special keys")
    void testEqualityForNonSpecialKeys() {
        ServerFilter.FilterKey key1 = new ServerFilter.FilterKey("full");
        ServerFilter.FilterKey key2 = new ServerFilter.FilterKey("appId");
        ServerFilter.FilterKey key3 = new ServerFilter.FilterKey("appId");

        assertFalse(key1.isSpecial());
        assertFalse(key2.isSpecial());
        assertFalse(key3.isSpecial());
        assertNotEquals(key1, key2);
        assertNotEquals(key1.id, key2.id);
        assertNotEquals(key1.id, key3.id);
        assertNotEquals(key1, key3);
        assertEquals(key2, key3);
        assertEquals(key2.hashCode(), key3.hashCode());
        assertNotEquals(key1.hashCode(), key2.hashCode());
        assertNotEquals(key1.hashCode(), key3.hashCode());
    }

    @Test
    @DisplayName("Test duplicate filter entries for Non-Special keys")
    void testDuplicateFilterEntriesForNonSpecialKeys() {
        ServerFilter filter = ServerFilter.create().appId(550).appId(730);

        assertEquals("\\appId\\730", filter.toString());

        log.debug("Output #1 = {}", filter.toString());

        filter.isFull(true);

        assertEquals("\\appId\\730\\full\\1", filter.toString());

        log.debug("Output #2 = {}", filter.toString());

        filter.isFull(false);

        log.debug("Output #3 = {}", filter.toString());

        assertEquals("\\appId\\730\\full\\0", filter.toString());
    }

    @Test
    @DisplayName("Test duplicate filter entries for Special keys")
    void testDuplicateFilterEntriesForSpecialKeys() {
        ServerFilter filter = ServerFilter.create()
                .appId(550)
                .nor()
                .dedicated(true)
                .nor()
                .isEmpty(true)
                .nand()
                .hasVersion("*")
                .napp(550)
                .isWhitelisted(true)
                .napp(730);

        log.debug("Output #1 = {}", filter.toString());
        assertEquals("\\appId\\550\\nor\\dedicated\\1\\nor\\empty\\1\\nand\\version_match\\*\\napp\\550\\white\\1\\napp\\730", filter.toString());
    }

    @Test
    @DisplayName("Test null appId argument")
    void testNullAppId() {
        ServerFilter filter = ServerFilter.create().appId(null);
        assertTrue(filter.toString().isEmpty());
        log.debug("Output #1 = {}", filter.toString());
    }

    @Test
    @DisplayName("Test null argument for all boolean properties")
    void testNullBooleanProperties() {
        ServerFilter filter = ServerFilter.create()
                .isWhitelisted(null)
                .isEmpty(null)
                .isFull(null)
                .isLinuxServer(null)
                .isPasswordProtected(null)
                .isSecure(null)
                .isSpecProxy(null);
        assertNotNull(filter.toString());
        assertTrue(filter.toString().isEmpty());
        assertDoesNotThrow((Executable) filter::toString);

        log.debug("Output = {}", filter.toString());
    }

    @Test
    void testAllServersFilter() {
        ServerFilter filter = ServerFilter.create()
                .appId(550)
                .nor()
                .dedicated(true)
                .nor()
                .isEmpty(true)
                .allServers()
                .nand()
                .hasVersion("*")
                .napp(550)
                .isWhitelisted(true)
                .napp(730);

        log.debug("Output = {}", filter.toString());

        assertTrue(filter.toString().isEmpty());
    }
}