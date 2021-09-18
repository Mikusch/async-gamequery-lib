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

package com.ibasco.agql.core;

import com.ibasco.agql.core.exceptions.PacketSizeLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * A container class for handling split-packets
 */
public class SplitPacketContainer implements Iterable<Map.Entry<Integer, byte[]>> {

    private static final Logger log = LoggerFactory.getLogger(SplitPacketContainer.class);

    private final TreeMap<Integer, byte[]> container = new TreeMap<>();

    private final int maxSplitPackets;

    /**
     * Primary Constructor
     *
     * @param maxSplitPackets
     *         The number of split packets we expect to receive
     */
    public SplitPacketContainer(int maxSplitPackets) {
        this.maxSplitPackets = maxSplitPackets;
    }

    /**
     * <p>Adds a split packet to the container</p>
     *
     * @param packetNumber
     *         The packet number
     * @param packet
     *         A byte array representing a single split-packet unit
     *
     * @throws PacketSizeLimitException
     *         thrown if the max number of allowed packets has been reached. This is usually specified by the protocol.
     */
    public void addPacket(int packetNumber, byte[] packet) {
        if (this.container.size() >= maxSplitPackets)
            throw new PacketSizeLimitException(String.format("You can no longer add any more packets. (Limit: of %d packets has been reached))", this.maxSplitPackets));
        this.container.put(packetNumber, packet);
    }

    /**
     * @param packetNumber
     *         The packet number to be removed
     */
    public void removePacket(int packetNumber) {
        this.container.remove(packetNumber);
    }

    /**
     * <p>Iterate each packet entry</p>
     *
     * @param action
     *         A {@link Consumer} callback
     */
    public void forEachEntry(Consumer<? super Map.Entry<Integer, byte[]>> action) {
        container.entrySet().forEach(action);
    }

    /**
     * Verify that we received all packets and no packets are missing
     *
     * @return Returns true if we have complete packets
     */
    public boolean isComplete() {
        return maxSplitPackets == container.size();
    }

    /**
     * <p>Returns the sum of each packets within this container</p>
     *
     * @return The total number of packets within this container
     */
    public int getPacketSize() {
        return container.values()
                        .stream()
                        .mapToInt(bytes -> bytes.length)
                        .sum();
    }

    @Override
    public Iterator<Map.Entry<Integer, byte[]>> iterator() {
        return container.entrySet().iterator();
    }
}
