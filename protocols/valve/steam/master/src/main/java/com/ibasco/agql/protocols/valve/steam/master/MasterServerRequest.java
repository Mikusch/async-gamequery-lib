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

package com.ibasco.agql.protocols.valve.steam.master;

import com.ibasco.agql.core.AbstractGameServerRequest;
import com.ibasco.agql.core.utils.ServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.packets.MasterServerRequestPacket;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/15/2016.
 */
public class MasterServerRequest<T> extends AbstractGameServerRequest<MasterServerRequestPacket> {

    private MasterServerRegion region;

    private ServerFilter filter;
    private InetSocketAddress startIp;

    public MasterServerRequest(InetSocketAddress recipient, MasterServerRegion region, ServerFilter filter, InetSocketAddress startIp) {
        super(recipient);
        this.region = region;
        this.filter = filter;
        this.startIp = startIp;
    }

    @Override
    public MasterServerRequestPacket getMessage() {
        return new MasterServerRequestPacket(region, filter, startIp);
    }
}
