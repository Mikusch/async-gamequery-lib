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

package com.ibasco.agql.protocols.valve.steam.master.client;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.ibasco.agql.core.client.AbstractGameServerClient;
import com.ibasco.agql.core.exceptions.AsyncGameLibUncheckedException;
import com.ibasco.agql.core.functions.TriConsumer;
import com.ibasco.agql.core.utils.ConcurrentUtils;
import com.ibasco.agql.core.utils.ServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerMessenger;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerRequest;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerResponse;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>Queries Valve Master Server to retrieve a list of game servers</p>
 */
public class MasterServerQueryClient extends AbstractGameServerClient<MasterServerRequest, MasterServerResponse, MasterServerMessenger> {

    private static final Logger log = LoggerFactory.getLogger(MasterServerQueryClient.class);

    private static final int DEFAULT_MAX_RETRIES = 3;

    private Retryer<Vector<InetSocketAddress>> retryer;

    private final AtomicBoolean retryOnTimeout = new AtomicBoolean(false);

    private int maxRetries;

    /**
     * Create a new {@link MasterServerQueryClient} instance with the default parameters
     */
    public MasterServerQueryClient() {
        this(null);
    }

    /**
     * Create new {@link MasterServerQueryClient} with a custom {@link ExecutorService}
     *
     * @param executorService
     *         The {@link ExecutorService} to be used by the underlying network transport service
     */
    public MasterServerQueryClient(ExecutorService executorService) {
        this(false, DEFAULT_MAX_RETRIES, executorService);
    }

    /**
     * Create new {@link MasterServerQueryClient} with the default {@link ExecutorService}
     *
     * @param retryOnTimeout
     *         Set to <code>true</code> to retry re-sending the master server request when a timeout occurs
     * @param maxRetries
     *         The maximum number of attempts to send the master server request on the event of a timeout
     */
    public MasterServerQueryClient(boolean retryOnTimeout, int maxRetries) {
        this(retryOnTimeout, maxRetries, null);
    }

    /**
     * Create new {@link MasterServerQueryClient} with a custom {@link ExecutorService}
     *
     * @param retryOnTimeout
     *         Set to <code>true</code> to retry re-sending the master server request when a timeout occurs
     * @param maxRetries
     *         The maximum number of attempts to send the master server request on the event of a timeout
     * @param executorService
     *         The {@link ExecutorService} to be used by the underlying network transport service
     */
    public MasterServerQueryClient(boolean retryOnTimeout, int maxRetries, ExecutorService executorService) {
        super(new MasterServerMessenger(executorService));
        this.retryOnTimeout.set(retryOnTimeout);
        this.maxRetries = maxRetries <= 0 ? 1 : maxRetries;
        this.retryer = RetryerBuilder.<Vector<InetSocketAddress>>newBuilder()
                .retryIfExceptionOfType(TimeoutException.class)
                .withStopStrategy(StopStrategies.stopAfterAttempt(this.maxRetries))
                .build();
    }

    /**
     * <p>A helper to determine if the address is a terminator type address</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return true if the {@link InetSocketAddress} supplied is a terminator address
     */
    private static boolean isIpTerminator(InetSocketAddress address) {
        return "0.0.0.0".equals(address.getAddress().getHostAddress()) && address.getPort() == 0;
    }

    /**
     * <p>Retrieves a list of servers from the Steam Master Server.</p>
     *
     * @param region
     *         A {@link MasterServerRegion} value that specifies which server region the master server should return
     * @param filter
     *         A {@link ServerFilter} representing a set of filters to be used by the query
     *
     * @return A {@link CompletableFuture} containing a {@link Vector} of {@link InetSocketAddress}.
     *
     * @see #getServerList(MasterServerType, MasterServerRegion, ServerFilter, TriConsumer)
     */
    public CompletableFuture<Vector<InetSocketAddress>> getServerList(final MasterServerType type, final MasterServerRegion region, final ServerFilter filter) {
        return getServerList(type, region, filter, null);
    }

    /**
     * <p>Retrieves a list of servers from the Steam Master Server.</p>
     *
     * @param type
     *         A {@link MasterServerType} to indicate which type of servers the master server should return
     * @param region
     *         A {@link MasterServerRegion} value that specifies which server region the master server should return
     * @param filter
     *         A {@link ServerFilter} representing a set of filters to be used by the query
     * @param callback
     *         A {@link TriConsumer} that will be invoked repeatedly for partial response
     *
     * @return A {@link CompletableFuture} containing a {@link Vector} of {@link InetSocketAddress}.
     *
     * @see #getServerList(MasterServerType, MasterServerRegion, ServerFilter)
     */
    public CompletableFuture<Vector<InetSocketAddress>> getServerList(final MasterServerType type, final MasterServerRegion region, final ServerFilter filter, final TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> callback) {
        //As per protocol specs, this get required as our starting seed address
        InetSocketAddress startAddress = new InetSocketAddress("0.0.0.0", 0);
        ExecutorService service = ObjectUtils.defaultIfNull(getMessenger().getExecutorService(), ForkJoinPool.commonPool());
        return CompletableFuture.supplyAsync(() -> this.getServersFromStartAddress(startAddress, type, region, filter, callback), service);
    }

    /**
     * <p>A blocking function to retrieve a list of servers from the valve master server.</p>
     *
     * @param startAddress
     *         The start IP Address. Use 0.0.0.0:0 to get a list from the beggining.
     * @param type
     *         A {@link MasterServerType} to indicate which type of servers the master server should return
     * @param region
     *         A {@link MasterServerRegion} value that specifies which server region the master server should return
     * @param filter
     *         A {@link ServerFilter} representing a set of filters to be used by the query
     * @param callback
     *         A {@link TriConsumer} that will be invoked repeatedly for partial response
     *
     * @return A {@link Vector} containing the server addressess retrieved from the master
     */
    private Vector<InetSocketAddress> getServersFromStartAddress(InetSocketAddress startAddress, MasterServerType type, MasterServerRegion region, ServerFilter filter, final TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> callback) {
        final Vector<InetSocketAddress> serverMasterList = new Vector<>();
        final InetSocketAddress destination = type.getMasterAddress();
        final AtomicBoolean done = new AtomicBoolean(false);

        final AtomicReference<InetSocketAddress> _startAddress = new AtomicReference<>(startAddress);

        while (!done.get()) {
            log.debug("Getting from master server with seed : " + _startAddress);
            try {
                log.debug("Sending master source with seed: {}:{}, Filter: {}", _startAddress.get().getAddress().getHostAddress(), _startAddress.get().getPort(), filter);

                final Vector<InetSocketAddress> serverList;

                if (retryOnTimeout.get()) {
                    //Send initial query to the master source
                    serverList = retryer.call(() -> {
                        log.debug("Trying to send request packet to master server");
                        CompletableFuture<Vector<InetSocketAddress>> p = sendRequest(new MasterServerRequest(destination, region, filter, _startAddress.get()));
                        return p.get(3, TimeUnit.SECONDS);
                    });
                } else {
                    final CompletableFuture<Vector<InetSocketAddress>> p = sendRequest(new MasterServerRequest(destination, region, filter, _startAddress.get()));
                    //Retrieve the first batch, timeout after 3 seconds
                    serverList = p.get(3, TimeUnit.SECONDS);
                }

                //Iterate through each address and call onComplete responseCallback. Make sure we don't include the last source ip received
                final InetSocketAddress lastServerIp = _startAddress.get();

                //Filter the address entries and make sure we do not include the last server ip received
                serverList.stream().filter(inetSocketAddress -> (!inetSocketAddress.equals(lastServerIp))).forEachOrdered(ip -> {
                    if (callback != null && !isIpTerminator(ip)) {
                        callback.accept(ip, destination, null);
                    }
                    //Add a fixed delay here. We shouldn't send requests too fast to the master server
                    // there is a chance that we might not receive the end of the list.
                    ConcurrentUtils.sleepUninterrupted(13);
                    serverMasterList.add(ip);
                });

                //Retrieve the last element of the source list and use it as the next seed for the next query
                _startAddress.set(serverList.lastElement());

                log.debug("Last Server IP Received: {}:{}", _startAddress.get().getAddress().getHostAddress(), _startAddress.get().getPort());

                //Did the master send a terminator address?
                // If so, mark as complete
                if (isIpTerminator(_startAddress.get())) {
                    log.debug("Reached the end of the server list");
                    done.set(true);
                }
                //Thread.sleep(10);
            } catch (RetryException | InterruptedException | TimeoutException e) {
                log.debug("Timeout/Thread Interruption/ExecutionException Occured during retrieval of server list from master", e);
                done.set(true); //stop looping if we receive a timeout
                if (callback != null)
                    callback.accept(null, destination, e);
            } catch (ExecutionException e) {
                log.debug("Exception occured during execution", e);
                if (callback != null)
                    callback.accept(null, destination, e);
                throw new AsyncGameLibUncheckedException(e.getCause());
            }
        } //while

        log.debug("Got a total list of {} servers from master", serverMasterList.size());

        //Returns the complete server list retrieved from the master server
        return serverMasterList;
    }

    public boolean isRetryOnTimeout() {
        return retryOnTimeout.get();
    }

    public int getMaxRetries() {
        return maxRetries;
    }
}
