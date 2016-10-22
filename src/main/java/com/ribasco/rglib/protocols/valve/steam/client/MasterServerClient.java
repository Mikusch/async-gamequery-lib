/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.rglib.protocols.valve.steam.client;

import com.ribasco.rglib.core.Callback;
import com.ribasco.rglib.core.client.GameServerQueryClient;
import com.ribasco.rglib.core.enums.RequestPriority;
import com.ribasco.rglib.core.utils.ConcurrentUtils;
import com.ribasco.rglib.protocols.valve.steam.MasterServerFilter;
import com.ribasco.rglib.protocols.valve.steam.MasterServerMessenger;
import com.ribasco.rglib.protocols.valve.steam.enums.MasterServerRegion;
import com.ribasco.rglib.protocols.valve.steam.enums.MasterServerType;
import com.ribasco.rglib.protocols.valve.steam.request.master.MasterServerRequest;
import com.ribasco.rglib.protocols.valve.steam.response.master.MasterServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MasterServerClient extends GameServerQueryClient<MasterServerRequest, MasterServerResponse, MasterServerMessenger> {
    private static final Logger log = LoggerFactory.getLogger(MasterServerClient.class);

    public MasterServerClient() {
        super(new MasterServerMessenger());
    }

    /**
     * <p>Retrieves a list of servers from the Steam Master Server.</p>
     *
     * @param region A {@link MasterServerRegion} value that specifies which server region the master server should return
     * @param filter A {@link MasterServerFilter} representing a set of filters to be used by the query
     *
     * @return A {@link CompletableFuture} that contains a {@link java.util.Set} of servers retrieved from the master
     *
     * @see #getServerList(MasterServerType, MasterServerRegion, MasterServerFilter, Callback)
     */
    public CompletableFuture<Vector<InetSocketAddress>> getServerList(final MasterServerType type, final MasterServerRegion region, final MasterServerFilter filter) {
        return getServerList(type, region, filter, null);
    }

    /**
     * <p>Retrieves a list of servers from the Steam Master Server.</p>
     *
     * @param type     A {@link MasterServerType} to indicate which type of servers the master server should return
     * @param region   A {@link MasterServerRegion} value that specifies which server region the master server should return
     * @param filter   A {@link MasterServerFilter} representing a set of filters to be used by the query
     * @param callback A {@link Callback} that will be invoked when a response has been received
     *
     * @return A {@link CompletableFuture} that contains a {@link java.util.Set} of servers retrieved from the master
     *
     * @see #getServerList(MasterServerType, MasterServerRegion, MasterServerFilter)
     */
    public CompletableFuture<Vector<InetSocketAddress>> getServerList(final MasterServerType type, final MasterServerRegion region, final MasterServerFilter filter, final Callback<InetSocketAddress> callback) {
        //As per protocol specs, this get required as our starting seed address
        InetSocketAddress startAddress = new InetSocketAddress("0.0.0.0", 0);

        final CompletableFuture<Vector<InetSocketAddress>> masterPromise = new CompletableFuture<>();
        final Vector<InetSocketAddress> serverMasterList = new Vector<>();
        final InetSocketAddress destination = type.getMasterAddress();
        final AtomicBoolean done = new AtomicBoolean(false);

        while (!done.get()) {
            log.debug("Getting from master server with seed : " + startAddress);
            try {
                log.info("Sending master source with seed: {}:{}, Filter: {}", startAddress.getAddress().getHostAddress(), startAddress.getPort(), filter);

                //Send initial query to the master source
                final CompletableFuture<Vector<InetSocketAddress>> p = sendRequest(new MasterServerRequest(destination, region, filter, startAddress), RequestPriority.HIGH);

                //Retrieve the first batch, timeout after 3 seconds
                final Vector<InetSocketAddress> serverList = p.get(3000, TimeUnit.MILLISECONDS);

                //Iterate through each address and call onComplete responseCallback. Make sure we don't include the last source ip received
                final InetSocketAddress lastServerIp = startAddress;

                //With streams, we can easily filter out the unwanted entries. (e.g. Excluding the last source ip received)
                serverList.stream().filter(inetSocketAddress -> (!inetSocketAddress.equals(lastServerIp))).forEachOrdered(ip -> {
                    if (callback != null && !isIpTerminator(ip))
                        callback.onComplete(ip, destination, null);
                    //Add a delay here. We shouldn't send requests too fast to the master server
                    // there is a high chance that we might not receive the end of the list.
                    ConcurrentUtils.sleepUninterrupted(13);
                    serverMasterList.add(ip);
                });

                //Retrieve the last element of the source list and use it as the next seed for the next query
                startAddress = serverList.lastElement();

                log.debug("Last Server IP Received: {}:{}", startAddress.getAddress().getHostAddress(), startAddress.getPort());

                //Did the master send a terminator address?
                // If so, mark as complete
                if (isIpTerminator(startAddress)) {
                    log.debug("Reached the end of the server list");
                    done.set(true);
                }
                //Thread.sleep(serverList.size() * 15);
            } catch (InterruptedException | TimeoutException e) {
                log.error("Timeout/Thread Interruption/ExecutionException Occured during retrieval of server list from master");
                done.set(true); //stop looping if we receive a timeout
                if (callback != null)
                    callback.onComplete(null, destination, e);
                masterPromise.completeExceptionally(e);
            } catch (ExecutionException e) {
                log.error("ExecutionException occured {}", e);
                if (callback != null)
                    callback.onComplete(null, destination, e);
                masterPromise.completeExceptionally(e);
            }
        } //while

        log.debug("Got a total list of {} servers from master", serverMasterList.size());

        //Returns the complete server list retrieved from the master server
        if (!masterPromise.isDone() && !masterPromise.isCompletedExceptionally())
            masterPromise.complete(serverMasterList);

        return masterPromise;
    }

    /**
     * <p>A helper to determine if the address is a terminator type address</p>
     *
     * @param address The {@link InetSocketAddress} of the source server
     *
     * @return true if the {@link InetSocketAddress} supplied is a terminator address
     */
    private static boolean isIpTerminator(InetSocketAddress address) {
        return "0.0.0.0".equals(address.getAddress().getHostAddress()) && address.getPort() == 0;
    }
}
