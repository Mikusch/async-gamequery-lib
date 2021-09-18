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

package com.ibasco.agql.protocols.valve.source.query.client;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ibasco.agql.core.client.AbstractGameServerClient;
import com.ibasco.agql.core.enums.ProcessingMode;
import com.ibasco.agql.core.exceptions.CacheTimeoutException;
import com.ibasco.agql.protocols.valve.source.query.SourceQueryMessenger;
import com.ibasco.agql.protocols.valve.source.query.SourceServerRequest;
import com.ibasco.agql.protocols.valve.source.query.SourceServerResponse;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceChallengeType;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourcePlayer;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import com.ibasco.agql.protocols.valve.source.query.request.SourceChallengeRequest;
import com.ibasco.agql.protocols.valve.source.query.request.SourceInfoRequest;
import com.ibasco.agql.protocols.valve.source.query.request.SourcePlayerRequest;
import com.ibasco.agql.protocols.valve.source.query.request.SourceRulesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * A client used for querying information on Source servers. Based on the Valve Source Query Protocol.
 *
 * @see <a href="https://developer.valvesoftware.com/wiki/Server_Queries#Source_Server">Valve Source Server Query
 * Protocol</a>
 */
public class SourceQueryClient extends AbstractGameServerClient<SourceServerRequest, SourceServerResponse, SourceQueryMessenger> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryClient.class);

    private static final int MAX_CHALLENGE_CACHE_SIZE = 32000;

    private final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("cache-pool-%d").setDaemon(true).build();

    private final ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1, threadFactory));

    private LoadingCache<InetSocketAddress, Integer> challengeCache;

    private int maxCacheSize = MAX_CHALLENGE_CACHE_SIZE;

    private Duration cacheExpiration = Duration.ofMinutes(15);

    private Duration cacheRefreshInterval = Duration.ofMinutes(10);

    /**
     * Default Constructor using the {@link SourceQueryMessenger}
     */
    public SourceQueryClient() {
        this(null);
    }

    /**
     * Create a new {@link SourceQueryClient} with a custom {@link ExecutorService}
     *
     * @param executorService
     *         The {@link ExecutorService} to be used by the underlying network transport service
     */
    public SourceQueryClient(ExecutorService executorService) {
        super(new SourceQueryMessenger(ProcessingMode.ASYNCHRONOUS, executorService));
    }

    /**
     * <p>Retrieves a Server Challenge number from the server. This is used for some requests (such as PLAYERS and
     * RULES) that requires a challenge number.</p>
     *
     * @param type
     *         A {@link SourceChallengeType}
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} returning a value of {@link Integer} representing the server challenge number
     */
    public CompletableFuture<Integer> getServerChallenge(SourceChallengeType type, InetSocketAddress address) {
        return sendRequest(new SourceChallengeRequest(type, address));
    }

    /**
     * <p>Retrieves a challenge number from the internal cache if available.  If the challenge number does not yet exist
     * in the cache then a
     * request (using: {@link #getServerChallenge(SourceChallengeType, InetSocketAddress)}) will be issued to the server
     * to obtain the latest challenge number.</p>
     * <p>This is considered to be thread-safe.</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} containing the resulting challenge {@link Integer}
     *
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     */
    public synchronized CompletableFuture<Integer> getServerChallengeFromCache(InetSocketAddress address) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getChallengeCache().get(address);
            } catch (ExecutionException e) {
                throw new CacheTimeoutException(e.getCause());
            }
        }, executorService);
    }

    /**
     * <p>Retrieve source server rules information. Please note that this method sends an initial challenge request
     * (Total of 5 bytes) to the server using {@link #getServerChallenge(SourceChallengeType, InetSocketAddress)}.
     * If you plan to use this method more than once for the same address, please consider using {@link
     * #getServerRulesCached(InetSocketAddress)} instead.
     * </p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     */
    public CompletableFuture<Map<String, String>> getServerRules(InetSocketAddress address) {
        return getServerChallenge(SourceChallengeType.RULES, address)
                .thenCompose(challenge -> getServerRules(challenge, address));
    }

    /**
     * <p>
     * Retrieve source server rules information. You NEED to obtain a valid challenge number from the server first.
     * </p>
     *
     * @param challenge
     *         The challenge number to be used for the request
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     *
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<Map<String, String>> getServerRules(int challenge, InetSocketAddress address) {
        return sendRequest(new SourceRulesRequest(challenge, address));
    }

    /**
     * <p>Retrieve source server rules information. Uses the internal cache to retrieve the challenge number (if
     * available).</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     *
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<Map<String, String>> getServerRulesCached(InetSocketAddress address) {
        return getServerChallengeFromCache(address)
                .thenCompose(challengeFromCache -> getServerRules(challengeFromCache, address));
    }

    /**
     * <p>
     * Retrieve a list of active players in the server. Please note that this method sends an initial
     * challenge request (Total of 5 bytes) to the server using {@link #getServerChallenge(SourceChallengeType,
     * InetSocketAddress)}.
     * If you plan to use this method more than once for the same address, please consider using {@link
     * #getPlayersCached(InetSocketAddress)} instead.
     * </p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on
     * the server
     *
     * @see #getPlayers(int, InetSocketAddress)
     */
    public CompletableFuture<List<SourcePlayer>> getPlayers(InetSocketAddress address) {
        return getServerChallenge(SourceChallengeType.PLAYER, address)
                .thenCompose(challenge -> getPlayers(challenge, address));
    }

    /**
     * <p>Retrieve a list of active players in the server. You NEED to obtain a valid challenge number from the server
     * first.</p>
     *
     * @param challenge
     *         The challenge number to be used for the request
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on
     * the server
     *
     * @see #getPlayers(InetSocketAddress)
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<List<SourcePlayer>> getPlayers(int challenge, InetSocketAddress address) {
        return sendRequest(new SourcePlayerRequest(challenge, address));
    }

    /**
     * <p>Retrieve a list of active players in the server. This uses the internal cache to retrieve the challenge number
     * (if available).</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on
     * the server
     *
     * @see #getPlayers(int, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<List<SourcePlayer>> getPlayersCached(InetSocketAddress address) {
        return getServerChallengeFromCache(address)
                .thenCompose(challenge -> getPlayers(challenge, address));
    }

    /**
     * <p>Retrieves information of the Source Server</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains {@link SourceServer} instance
     */
    public CompletableFuture<SourceServer> getServerInfo(InetSocketAddress address) {
        return sendRequest(new SourceInfoRequest(address));
    }

    /**
     * @return The maxmimum size allowable for the internal challenge cache
     */
    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    /**
     * Sets the maximum allowable size for the internal challenge cache
     *
     * @param maxCacheSize
     *         An int representing the cache size
     */
    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    /**
     * @return Returns the duration (in minutes) used by the internal cache service to expire entries.
     */
    public long getCacheExpiration() {
        return cacheExpiration.toMinutes();
    }

    /**
     * @param cacheExpiration
     *         A {@link Duration} instance representing the time it will take to expire cached entries
     */
    public void setCacheExpiration(Duration cacheExpiration) {
        this.cacheExpiration = cacheExpiration;
    }

    /**
     * @param cacheExpiration
     *         A number representing the duration (in minutes) to expire cached entries
     */
    public void setCacheExpiration(long cacheExpiration) {
        setCacheExpiration(Duration.ofMinutes(cacheExpiration));
    }

    /**
     * @return A {@link Duration} representing the time it will take to refersh a challenge in the internal cache
     */
    public Duration getCacheRefreshInterval() {
        return cacheRefreshInterval;
    }

    /**
     * Sets the refresh interval of each cached entry
     *
     * @param cacheRefreshInterval
     *         The {@link Duration} representing the refresh interval for each cached entry
     */
    public void setCacheRefreshInterval(Duration cacheRefreshInterval) {
        this.cacheRefreshInterval = cacheRefreshInterval;
    }

    /**
     * Retrieve the internal challenge cache of this instance
     *
     * @return A {@link LoadingCache} containing a map of {@link InetSocketAddress} keys and {@link Integer} values.
     */
    public synchronized LoadingCache<InetSocketAddress, Integer> getChallengeCache() {
        //lazy-loaded
        if (challengeCache == null) {
            log.debug("Building new cache...");
            challengeCache = CacheBuilder.newBuilder()
                    .maximumSize(maxCacheSize)
                    .expireAfterWrite(cacheExpiration.toMillis(), TimeUnit.MILLISECONDS)
                    .refreshAfterWrite(cacheRefreshInterval.toMillis(), TimeUnit.MILLISECONDS)
                    //.recordStats()
                    .build(new CacheLoader<>() {
                        @Override
                        public Integer load(InetSocketAddress key) throws Exception {
                            return getServerChallenge(SourceChallengeType.ANY, key).get();
                        }

                        @Override
                        public ListenableFuture<Integer> reload(InetSocketAddress key, Integer oldValue) throws Exception {
                            log.debug("Refreshing challenge number for : {}, Old Value = {}", key, oldValue);

                            final CompletableFuture<Integer> cf = getServerChallenge(SourceChallengeType.ANY, key);

                            //noinspection NullableProblems
                            return new ListenableFuture<>() {
                                private Map<Runnable, Executor> listeners = new HashMap<>();

                                {
                                    cf.whenComplete((integer, ex) -> {
                                        if (ex != null) {

                                            return;
                                        }
                                        for (Map.Entry<Runnable, Executor> e : listeners.entrySet()) {
                                            e.getValue().execute(e.getKey());
                                        }
                                    });
                                }

                                @Override
                                public void addListener(Runnable listener, Executor executor) {
                                    listeners.put(listener, executor);
                                }

                                @Override
                                public boolean cancel(boolean mayInterruptIfRunning) {
                                    return cf.cancel(mayInterruptIfRunning);
                                }

                                @Override
                                public boolean isCancelled() {
                                    return cf.isCancelled();
                                }

                                @Override
                                public boolean isDone() {
                                    return cf.isDone();
                                }

                                @Override
                                public Integer get() throws InterruptedException, ExecutionException {
                                    return cf.get();
                                }

                                @Override
                                public Integer get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                                    return cf.get(timeout, unit);
                                }
                            };//executorService.submit(() -> load(key));
                        }
                    });
        }
        return challengeCache;
    }

    /**
     * Method that tries to perform a graceful shutdown of the client
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        super.close();
        getChallengeCache().cleanUp();
        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Interrupted Shutdown", e);
        }
    }
}
