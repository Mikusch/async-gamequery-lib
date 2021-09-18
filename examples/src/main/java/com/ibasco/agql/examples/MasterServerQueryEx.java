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

import com.ibasco.agql.core.utils.ServerFilter;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.steam.master.client.MasterServerQueryClient;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MasterServerQueryEx extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(MasterServerQueryEx.class);

    private MasterServerQueryClient masterServerQueryClient;

    public static void main(String[] args) throws Exception {
        MasterServerQueryEx app = new MasterServerQueryEx();
        app.run();
    }

    @Override
    public void run() throws Exception {
        masterServerQueryClient = new MasterServerQueryClient();
        this.listAllServers();
    }

    public void close() throws IOException {
        masterServerQueryClient.close();
    }

    public void listAllServers() {
        int appId = Integer.valueOf(promptInput("Please enter an App ID (optional): ", false, "-1"));

        ServerFilter filter = ServerFilter.create().dedicated(true).isEmpty(false);

        if (appId > 0)
            filter.appId(appId);

        log.info("Displaying Non-Empty servers for App Id: {}", (appId > 0) ? appId : "N/A");
        masterServerQueryClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, this::displayIpStream).join();
        log.info("Done");
    }

    public void displayIpStream(InetSocketAddress address, InetSocketAddress sender, Throwable error) {
        log.info("Server : {}", address);
    }
}
