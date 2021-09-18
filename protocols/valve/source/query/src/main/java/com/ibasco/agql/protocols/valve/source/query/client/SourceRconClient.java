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

import com.ibasco.agql.core.AbstractClient;
import com.ibasco.agql.core.utils.EncryptUtils;
import com.ibasco.agql.protocols.valve.source.query.SourceRconAuthStatus;
import com.ibasco.agql.protocols.valve.source.query.SourceRconMessenger;
import com.ibasco.agql.protocols.valve.source.query.SourceRconRequest;
import com.ibasco.agql.protocols.valve.source.query.SourceRconResponse;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconCmdException;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.exceptions.SourceRconAuthException;
import com.ibasco.agql.protocols.valve.source.query.request.SourceRconAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.request.SourceRconCmdRequest;
import com.ibasco.agql.protocols.valve.source.query.utils.SourceRconUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * A client used for executing commands to the Source Server using the Valve RCON Protocol
 *
 * @see <a href="https://developer.valvesoftware.com/wiki/Source_RCON_Protocol">Source RCON Protocol Specifications</a>
 */
public class SourceRconClient extends AbstractClient<SourceRconRequest, SourceRconResponse, SourceRconMessenger> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconClient.class);

    private Boolean _reauth = null;

    private boolean reauthenticate = true;

    /**
     * Contains a map of authenticated request ids with the server address as the key
     */
    private Map<InetSocketAddress, String> credentialsMap;

    /**
     * Default Constructor. By default, terminating packets are sent for every command
     */
    public SourceRconClient() {
        this(true);
    }

    /**
     * Create new {@link SourceRconClient} using the default {@link ExecutorService}
     *
     * @param sendTerminatingPacket
     *         Set to <code>true</code> to send terminator packets for every command.
     *
     * @see #SourceRconClient(boolean, ExecutorService)
     */
    public SourceRconClient(boolean sendTerminatingPacket) {
        this(sendTerminatingPacket, null);
    }

    /**
     * Some games (e.g. Minecraft) do not support terminator packets, if this is the case and you get an
     * error after sending a command, try to disable this feature by setting the <code>sendTerminatingPacket</code> flag
     * to <code>false</code>.
     *
     * @param sendTerminatingPacket
     *         Set to <code>true</code> to send terminator packets for every command.
     * @param executorService
     *         The {@link ExecutorService} to be used by the underlying network transport service
     */
    public SourceRconClient(boolean sendTerminatingPacket, ExecutorService executorService) {
        super(new SourceRconMessenger(sendTerminatingPacket, executorService));
        credentialsMap = new ConcurrentHashMap<>();
    }

    /**
     * <p>Send an authentication request to the Server. Password credentials are stored into memory and can later be
     * re-used in case a re-authentication is needed.</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     * @param password
     *         A non-empty password {@link String}
     *
     * @return A {@link CompletableFuture} which returns a {@link SourceRconAuthStatus} that holds the status of the
     * authentication request.
     *
     * @throws IllegalArgumentException
     *         Thrown when the address or password supplied is empty or null
     */
    public CompletableFuture<SourceRconAuthStatus> authenticate(InetSocketAddress address, String password) {
        if (StringUtils.isBlank(password) || address == null)
            throw new IllegalArgumentException("Password or Address is empty or null");

        //Remove existing meta if exists
        if (this.credentialsMap.containsKey(address)) {
            log.debug("Existing auth meta found. Removing");
            this.credentialsMap.remove(address);
        }

        int id = SourceRconUtil.createRequestId();
        log.debug("[AUTH]: Request with id: {}", id);
        CompletableFuture<SourceRconAuthStatus> authRequestFuture = sendRequest(new SourceRconAuthRequest(address, id, password));
        authRequestFuture.whenComplete((status, error) -> {
            log.debug("[AUTH] Response : Status: {}, Error: {}", status, (error != null) ? error.getMessage() : "N/A");
            if (error != null) {
                throw new SourceRconAuthException(error);
            }
            if (status != null && status.isAuthenticated()) {
                this.credentialsMap.put(address, EncryptUtils.encrypt(password, EncryptUtils.retrieveKey()));
            }
        });
        return authRequestFuture;
    }

    /**
     * <p>Send a re-authentication request to the Server. This will only work if the client has been previously
     * authenticated.</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} which returns a {@link SourceRconAuthStatus} that holds the status of the
     * authentication request.
     *
     * @see #authenticate(InetSocketAddress, String)
     */
    public CompletableFuture<SourceRconAuthStatus> authenticate(InetSocketAddress address) {
        if (isAuthenticated(address)) {
            return this.authenticate(address, EncryptUtils.decrypt(this.credentialsMap.get(address), EncryptUtils.retrieveKey()));
        }
        return CompletableFuture.completedFuture(new SourceRconAuthStatus(false, String.format("Not yet authenticated from server %s.", address)));
    }

    /**
     * <p>Sends a command to the Source server. Authentication is REQUIRED</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     * @param command
     *         The {@link String} containing the command to be issued on the server
     *
     * @return A {@link CompletableFuture} which contains a response {@link String} returned by the server
     *
     * @throws RconNotYetAuthException
     *         thrown if not yet authenticated to the server
     * @see #authenticate(InetSocketAddress, String)
     */
    public CompletableFuture<String> execute(InetSocketAddress address, String command) throws RconNotYetAuthException {
        if (!isAuthenticated(address))
            throw new RconNotYetAuthException("You are not yet authorized to access the server's rcon interface. Please authenticate first.");

        final Integer id = SourceRconUtil.createRequestId();

        CompletableFuture<String> response;

        SourceRconCmdRequest request = new SourceRconCmdRequest(address, id, command);

        if (reauthenticate && (_reauth != null && _reauth)) {
            log.debug("Re-authenticating from server");
            response = this.authenticate(address).thenCompose(status -> {
                if (status.isAuthenticated())
                    return sendRequest(request);
                else
                    return CompletableFuture.completedFuture("Unable to re-authenticate from server");
            });
        } else {
            log.debug("Executing command '{}' using request id: {}", command, id);
            response = sendRequest(request);
        }

        if (response != null)
            response.whenComplete(this::reauthOnError);

        return response;
    }

    /**
     * Sets the _reauth flag to true on error
     *
     * @param response
     *         The rcon response {@link String}
     * @param err
     *         The error returned by the response
     */
    private void reauthOnError(String response, Throwable err) {
        if (err != null)
            throw new RconCmdException(err.getMessage(), err);
        _reauth = reauthenticate && (response != null && StringUtils.isBlank(response));
    }

    /**
     * Checks the internal authentication map if the specified address is authenticated by the server or not
     *
     * @param server
     *         An {@link InetSocketAddress} representing the server
     *
     * @return true if the address specified is already authenticated
     */
    public boolean isAuthenticated(InetSocketAddress server) {
        return credentialsMap.containsKey(server) && (credentialsMap.get(server) != null);
    }

    /**
     * @return <code>true</code> if the client should re-authenticate on error
     */
    public boolean isReauthenticate() {
        return reauthenticate;
    }

    /**
     * Re-authenticate from server on error
     *
     * @param reauthenticate
     *         Set to <code>true</code> to re-authenticate from server on error
     */
    public void setReauthenticate(boolean reauthenticate) {
        this.reauthenticate = reauthenticate;
    }
}
