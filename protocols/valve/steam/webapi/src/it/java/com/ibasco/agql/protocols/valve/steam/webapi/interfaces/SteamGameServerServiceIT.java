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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces;

import com.ibasco.agql.core.utils.ServerFilter;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SteamGameServerServiceIT {

    private static final Logger log = LoggerFactory.getLogger(SteamGameServerServiceIT.class);

    private SteamGameServerService gameServerService;

    private SteamWebApiClient client;

    private Long createdSteamId;

    private String createdLoginToken;

    private String key;

    @BeforeAll
    void setup() {
        key = System.getenv("steam.key");
        client = new SteamWebApiClient(key, Executors.newCachedThreadPool());
        gameServerService = new SteamGameServerService(client);
    }

    @Test
    @Order(1)
    @DisplayName("Get non-empty server list")
    void test01_GetServerList() {
        ServerFilter filter = ServerFilter.create().dedicated(true).appId(730);
        List<SteamSourceServer> serverList = gameServerService.getServerList(filter, 10).join();
        assertNotNull(serverList);
        assertEquals(10, serverList.size());
    }

    @Test
    @Order(2)
    @DisplayName("Get account list")
    void test02_GetAccountList() {
        SteamGameServerAccountDetails gameServerAccountDetails = gameServerService.getAccountList().join();
        assertNotNull(gameServerAccountDetails);
        assertNotNull(gameServerAccountDetails.getServers());
        assertTrue(gameServerAccountDetails.getServers().size() > 0);
        log.info("Got: {}", gameServerAccountDetails);
    }

    @Test
    @Order(3)
    @DisplayName("Create account")
    void test04_CreateAccount() {
        SteamCreateAccountResponse res = gameServerService.createAccount(550, "testCreateAccount()").join();
        assertNotNull(res);
        createdSteamId = res.getSteamid();
        createdLoginToken = res.getLoginToken();
        assertNotNull(createdSteamId);
        assertNotNull(createdLoginToken);
        log.info("Got: {}", res);
    }

    @Test
    @Order(4)
    @DisplayName("Get account public info")
    void test03_GetAccountPublicInfo() {
        SteamAccountPublicInfo accountPublicInfo = gameServerService.getAccountPublicInfo(createdSteamId).join();
        assertNotNull(accountPublicInfo);
        log.info("Got: {}", accountPublicInfo);
    }

    @Test
    @Order(5)
    @DisplayName("Query login token")
    void test05_QueryLoginToken() {
        SteamLoginTokenStatus token = gameServerService.queryLoginToken(createdLoginToken).join();
        assertNotNull(token);
        assertEquals(createdSteamId, token.getSteamid());
        log.info("Got: {}", token);
    }

    @Test
    @Order(6)
    @DisplayName("Reset login token")
    void test06_resetLoginToken() {
        SteamLoginToken newToken = gameServerService.resetLoginToken(createdSteamId).join();
        assertNotNull(createdSteamId);
        assertNotNull(newToken);
        assertNotNull(newToken.getLoginToken());
        assertTrue(newToken.getLoginToken().length() > 0);
        log.info("Got: {}", newToken);
    }

    @Test
    @Order(7)
    @DisplayName("Set memo")
    void test07_testSetMemo() {
        String memo = "Test memo from junit @ " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        Boolean res = gameServerService.setMemo(createdSteamId, memo).join();
        assertTrue(res);
        log.info("Got: {}", res);
    }

    @Test
    @Order(8)
    @DisplayName("Delete account")
    void test08_DeleteAccount() {
        Boolean res = gameServerService.deleteAccount(createdSteamId).join();
        log.info("Got: {}", res);
        assertNotNull(res);
        assertTrue(res);
    }
}
