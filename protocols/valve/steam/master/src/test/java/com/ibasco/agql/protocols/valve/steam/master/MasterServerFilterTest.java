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

package com.ibasco.agql.protocols.valve.steam.master;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

class MasterServerFilterTest {

    private static final Logger log = LoggerFactory.getLogger(MasterServerFilterTest.class);

    @Test
    @DisplayName("Test filter keys inside a map")
    void testFilterKeysInsideMap() {
        Map<MasterServerFilter.FilterKey, Object> filterMap = new HashMap<>();
        filterMap.put(new MasterServerFilter.FilterKey("appId"), 550);
        filterMap.put(new MasterServerFilter.FilterKey("napp"), null);
        filterMap.put(new MasterServerFilter.FilterKey("napp"), null);
        assertEquals(3, filterMap.size());
        assertTrue(filterMap.containsKey(new MasterServerFilter.FilterKey("appId")));
    }

    @Test
    @DisplayName("Test equality for special keys")
    void testEqualityForSpecialKeys() {
        MasterServerFilter.FilterKey key1 = new MasterServerFilter.FilterKey("nand");
        MasterServerFilter.FilterKey key2 = new MasterServerFilter.FilterKey("nand");

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
        MasterServerFilter.FilterKey key1 = new MasterServerFilter.FilterKey("full");
        MasterServerFilter.FilterKey key2 = new MasterServerFilter.FilterKey("appId");
        MasterServerFilter.FilterKey key3 = new MasterServerFilter.FilterKey("appId");

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
        MasterServerFilter filter = MasterServerFilter.create().appId(550).appId(730);

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
        MasterServerFilter filter = MasterServerFilter.create()
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
        MasterServerFilter filter = MasterServerFilter.create().appId(null);
        assertTrue(filter.toString().isEmpty());
        log.debug("Output #1 = {}", filter.toString());
    }

    @Test
    @DisplayName("Test null argument for all boolean properties")
    void testNullBooleanProperties() {
        MasterServerFilter filter = MasterServerFilter.create()
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
        MasterServerFilter filter = MasterServerFilter.create()
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