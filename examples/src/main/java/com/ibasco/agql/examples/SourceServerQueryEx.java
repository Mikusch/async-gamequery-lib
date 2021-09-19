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

package com.ibasco.agql.examples;

import com.ibasco.agql.core.utils.ConcurrentUtils;
import com.ibasco.agql.core.utils.ServerFilter;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.source.query.client.SourceQueryClient;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceChallengeType;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourcePlayer;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import com.ibasco.agql.protocols.valve.steam.master.client.MasterServerQueryClient;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class SourceServerQueryEx extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(SourceServerQueryEx.class);

    private static final Pattern PATTERN_IP = Pattern.compile("(?<ip>\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b)|\\:(?<port>\\d*)");

    private SourceQueryClient sourceQueryClient;

    private MasterServerQueryClient masterServerQueryClient;

    public static void main(String[] args) throws Exception {
        SourceServerQueryEx q = new SourceServerQueryEx();
        q.run();
    }

    public void performQuery() throws Exception {
        var queryAllServers = promptInputBool("Query all available servers? (y/n)", true, "y");

        double start = 0.0d, end = 0.0d;

        if (queryAllServers) {
            int appId = Integer.parseInt(promptInput("List servers only from this app id (int)", false, null, "srcQryAppId"));
            Boolean emptyServers = promptInputBool("List only empty servers? (y/n)", false, null, "srcQryEmptySvrs");
            Boolean passwordProtected = promptInputBool("List only passwd protected servers? (y/n)", false, null, "srcQryPassProtect");
            Boolean dedicatedServers = promptInputBool("List only dedicated servers (y/n)", false, "y", "srcQryDedicated");
            var filter = ServerFilter.create()
                                     .dedicated(dedicatedServers)
                                     .isPasswordProtected(passwordProtected)
                                     .allServers()
                                     .isEmpty(emptyServers);

            if (appId > 0)
                filter.appId(appId);

            log.info("Using filter: {}", filter.toString());

            start = System.currentTimeMillis();
            performQuery(filter);
            end = ((System.currentTimeMillis() - start) / 1000) / 60;
        } else {
            String addressString = promptInput("Enter the address of the server you want to query (<ip>:<port>)", true, "127.0.0.1:27015");
            InetSocketAddress address = parseAddress(addressString);

            log.info("Querying server address: {}:{}", address.getHostString(), address.getPort());
            start = System.currentTimeMillis();
            log.info("Server Info: {}", performQuery(address));
            end = ((System.currentTimeMillis() - start) / 1000) / 60;
        }

        log.info("Test Completed  in {} minutes", end);
    }

    private SourceServer performQuery(InetSocketAddress address) throws ExecutionException, InterruptedException {
        return sourceQueryClient.getServerInfo(address).whenComplete((sourceServer, serverInfoError) -> {
            if (serverInfoError != null) {
                log.error("[SERVER : ERROR] : {}", serverInfoError.getMessage());
                return;
            }
            log.info("[SERVER : INFO] : {}", sourceServer);
        }).get();
    }

    private void performQuery(ServerFilter filter) {
        final AtomicInteger masterServerCtr = new AtomicInteger(), masterError = new AtomicInteger();
        final AtomicInteger serverInfoCtr = new AtomicInteger(), serverInfoErr = new AtomicInteger();
        final AtomicInteger challengeCtr = new AtomicInteger(), challengeErr = new AtomicInteger();
        final AtomicInteger playersCtr = new AtomicInteger(), playersErr = new AtomicInteger();
        final AtomicInteger rulesCtr = new AtomicInteger(), rulesErr = new AtomicInteger();

        try {
            List<CompletableFuture<?>> requestList = new ArrayList<>();

            try {
                masterServerQueryClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, (serverAddress, masterServerSender, masterServerError) -> {
                    try {
                        if (masterServerError != null) {
                            log.error("[MASTER : ERROR] :  From: {} = {}", masterServerSender, masterServerError.getMessage());
                            masterError.incrementAndGet();
                            return;
                        }

                        log.info("[MASTER : INFO] : {}", serverAddress);
                        masterServerCtr.incrementAndGet();

                        CompletableFuture<SourceServer> infoFuture = sourceQueryClient.getServerInfo(serverAddress).whenComplete((sourceServer, serverInfoError) -> {
                            if (serverInfoError != null) {
                                log.error("[SERVER : ERROR] : {}", serverInfoError.getMessage());
                                serverInfoErr.incrementAndGet();
                                return;
                            }
                            serverInfoCtr.incrementAndGet();
                            log.info("[SERVER : INFO] : {}", sourceServer);
                        });

                        requestList.add(infoFuture);

                        //Get Challenge
                        CompletableFuture<Integer> challengeFuture = sourceQueryClient.getServerChallenge(SourceChallengeType.PLAYER, serverAddress)
                                                                                      .whenComplete((challenge, serverChallengeError) -> {
                                                                                          if (serverChallengeError != null) {
                                                                                              log.error("[CHALLENGE : ERROR] Message: '{}')", serverChallengeError.getMessage());
                                                                                              challengeErr.incrementAndGet();
                                                                                              return;
                                                                                          }
                                                                                          log.info("[CHALLENGE : INFO] Challenge '{}'", challenge);
                                                                                          challengeCtr.incrementAndGet();

                                                                                          CompletableFuture<List<SourcePlayer>> playersFuture = sourceQueryClient.getPlayers(challenge, serverAddress);

                                                                                          playersFuture.whenComplete((players, playerError) -> {
                                                                                              if (playerError != null) {
                                                                                                  log.error("[PLAYERS : ERROR] Message: '{}')", playerError.getMessage());
                                                                                                  playersErr.incrementAndGet();
                                                                                                  return;
                                                                                              }
                                                                                              playersCtr.incrementAndGet();
                                                                                              log.info("[PLAYERS : INFO] : Total Players for Server '{}' = {}", serverAddress, players.size());
                                                                                              players.forEach(player -> log.info("    Index: {}, Name: {}, Duration: {}, Score: {}", player.getIndex(), player.getName(), player.getDuration(), player.getScore()));
                                                                                          });
                                                                                          requestList.add(playersFuture);

                                                                                          CompletableFuture<Map<String, String>> rulesFuture = sourceQueryClient.getServerRules(challenge, serverAddress);
                                                                                          rulesFuture.whenComplete((rules, rulesError) -> {
                                                                                              if (rulesError != null) {
                                                                                                  log.error("[RULES : ERROR] Message: '{}')", rulesError.getMessage());
                                                                                                  rulesErr.incrementAndGet();
                                                                                                  return;
                                                                                              }
                                                                                              rulesCtr.incrementAndGet();
                                                                                              log.info("[RULES : INFO] Total # of Rules for Server '{}' = {}", serverAddress, rules.size());
                                                                                              rules.forEach((key, value) -> log.info("    {} = {}", key, value));
                                                                                          });
                                                                                          requestList.add(rulesFuture);
                                                                                      });
                        requestList.add(challengeFuture);
                    } catch (Exception e) {
                        log.error("Error occured inside the master list callback", e);
                    }
                }).get(); //masterServerList

                log.info("Waiting for {} requests to complete", requestList.size());
                var futures = requestList.toArray(new CompletableFuture[0]);
                CompletableFuture.allOf(futures).get();
            } catch (Exception e) {
                log.error("Error occured during processing", e);
            } finally {
                log.info("   Total Master Server Retrieved: {}", masterServerCtr);
                log.info("   Total Master Server Error: {}", masterError);
                log.info(" ");
                log.info("   Total Server Info Retrieved: {}", serverInfoCtr);
                log.info("   Total Server Info Error: {}", serverInfoErr);
                log.info(" ");
                log.info("   Total Challenge Numbers Received: {}", challengeCtr);
                log.info("   Total Challenge Error: {}", challengeErr);
                log.info(" ");
                log.info("   Total Player Records Received: {}", playersCtr);
                log.info("   Total Player Error: {}", playersErr);
                log.info(" ");
                log.info("   Total Rules Records Received: {}", rulesCtr);
                log.info("   Total Rules Error: {}", rulesErr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private InetSocketAddress parseAddress(String address) throws ParseException {
        if (address == null || address.isBlank())
            throw new ParseException("Address cannot be blank", 0);
        var matcher = PATTERN_IP.matcher(address);
        if (!matcher.find())
            throw new ParseException(String.format("Invalid address: '%s'", address), 0);
        String ip = matcher.group("ip");
        Integer port = null;
        if (matcher.find()) {
            String portStr = matcher.group("port");
            if (StringUtils.isBlank(portStr) || !StringUtils.isNumeric(portStr))
                throw new ParseException(String.format("Invalid port number '%s'", portStr), 0);
            port = Integer.parseInt(portStr);
        }
        if (port == null)
            port = 27015;
        return new InetSocketAddress(ip, port);
    }

    @Override
    public void run() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        //Inititalize client
        sourceQueryClient = new SourceQueryClient(executorService);
        masterServerQueryClient = new MasterServerQueryClient(executorService);

        log.info("NOTE: Depending on your selected criteria, the application may time some time to complete. You can review the log file(s) once the program exits.");

        ConcurrentUtils.sleepUninterrupted(500);

        this.performQuery();
    }

    @Override
    public void close() throws IOException {
        log.info("Closing");
        sourceQueryClient.close();
        masterServerQueryClient.close();
    }
}
