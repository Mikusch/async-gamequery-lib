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

import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.source.query.SourceRconAuthStatus;
import com.ibasco.agql.protocols.valve.source.query.client.SourceRconClient;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconNotYetAuthException;
import org.apache.commons.lang3.RegExUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SourceRconQueryEx extends BaseExample {
    private static final Logger log = LoggerFactory.getLogger(SourceRconQueryEx.class);
    private SourceRconClient sourceRconClient;
    private String password;
    private InetSocketAddress serverAddress;

    /**
     * For internal testing purposes
     */
    public static void main(String[] args) throws Exception {
        SourceRconQueryEx c = new SourceRconQueryEx();
        c.run();
    }

    @Override
    public void run() throws Exception {
        sourceRconClient = new SourceRconClient();
        this.testRcon();
    }

    @Override
    public void close() {
        try {
            sourceRconClient.close();
        } catch (IOException ignored) {
        }
    }

    public void testRcon() throws InterruptedException {
        String address = promptInput("Please enter the source server address", true, "", "sourceRconIp");
        int port = Integer.valueOf(promptInput("Please enter the server port", false, "27015", "sourceRconPort"));

        boolean authenticated = false;

        while (!authenticated) {

            serverAddress = new InetSocketAddress(address, port);
            if (!authenticated) {
                password = promptInput("Please enter the rcon password", true, "", "sourceRconPass");
                log.info("Connecting to server {}:{}, with password = {}", address, port, RegExUtils.replaceAll(password, ".", "*"));
                SourceRconAuthStatus authStatus = sourceRconClient.authenticate(serverAddress, password).join();
                if (!authStatus.isAuthenticated()) {
                    log.error("ERROR: Could not authenticate from server (Reason: {})", authStatus.getReason());
                } else {
                    log.debug("Successfully authenticated from server : {}", address);
                    authenticated = true;
                }
            }

            while (authenticated) {
                String command = promptInput("\nEnter rcon command: ", true);
                try {
                    sourceRconClient.execute(serverAddress, command).whenComplete(this::handleResponse).join();
                } catch (RconNotYetAuthException e) {
                    authenticated = false;
                    break;
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        }
    }

    private void handleResponse(String response, Throwable error) {
        if (error != null) {
            log.error("Error occured while executing command: {}", error.getMessage());
            return;
        }
        log.info("Received Reply ({} bytes): \n{}", response.length(), response);
    }
}
