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

package com.ibasco.agql.protocols.valve.steam.master.packets;

import com.ibasco.agql.core.Decodable;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerPacket;
import io.netty.buffer.ByteBuf;

import java.net.InetSocketAddress;
import java.util.Vector;

public class MasterServerResponsePacket extends MasterServerPacket implements Decodable<Vector<InetSocketAddress>> {
    private Vector<InetSocketAddress> servers;
    private StringBuffer ip;

    public MasterServerResponsePacket() {
        servers = new Vector<>();
        ip = new StringBuffer();
    }

    @Override
    public Vector<InetSocketAddress> toObject() {
        ByteBuf data = getPayloadBuffer();

        //Clear the list
        servers.clear();

        int firstOctet, secondOctet, thirdOctet, fourthOctet, portNumber;

        //Process the content containing list of source ips
        do {
            //Clear string
            ip.setLength(0);

            firstOctet = data.readByte() & 0xFF;
            secondOctet = data.readByte() & 0xFF;
            thirdOctet = data.readByte() & 0xFF;
            fourthOctet = data.readByte() & 0xFF;
            portNumber = data.readShort() & 0xFFFF;

            //Build our ip string
            ip.append(firstOctet).append(".")
                    .append(secondOctet).append(".")
                    .append(thirdOctet).append(".")
                    .append(fourthOctet);

            //Add to the list
            servers.add(new InetSocketAddress(ip.toString(), portNumber));

            //Append port number
            ip.append(":").append(portNumber);
        } while (data.readableBytes() > 0);

        return servers;
    }
}
