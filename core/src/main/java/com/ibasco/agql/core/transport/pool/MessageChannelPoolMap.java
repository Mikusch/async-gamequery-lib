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

package com.ibasco.agql.core.transport.pool;

import com.ibasco.agql.core.AbstractMessage;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolMap;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

/**
 * A {@link ChannelPoolMap} implementation that takes an {@link AbstractMessage} as a reference for the key-lookup.
 *
 * @param <M>
 *         An {@link AbstractMessage} that will be used as a reference for our key lookup
 * @param <K>
 *         The actual key that will be used for the underlying {@link ChannelPoolMap} implementation.
 *         The type of this key should be the same type returned by our key resolver.
 */
public class MessageChannelPoolMap<M extends AbstractMessage, K>
        implements ChannelPoolMap<M, ChannelPool>, Iterable<Map.Entry<K, ChannelPool>>, Closeable {

    private Function<M, K> keyResolver;
    private Function<K, ChannelPool> poolFactory;

    /**
     * The internal {@link ChannelPoolMap} implementation that use the actual key type for the map
     */
    private AbstractChannelPoolMap<K, ChannelPool> internalPoolMap = new AbstractChannelPoolMap<K, ChannelPool>() {
        @Override
        protected ChannelPool newPool(K key) {
            return poolFactory.apply(key);
        }
    };

    /**
     * <p>Accepts two functions that will be used internally for processing the key and creation of the {@link
     * ChannelPool} instance</p>
     *
     * @param keyResolver
     *         A function that accepts an {@link AbstractMessage} as the input and returns a type of a key (as
     *         specified).
     *         This will be used to resolve keys based on the {@link AbstractMessage} argument.
     * @param poolFactory
     *         A factory function that returns a {@link ChannelPool} implementation based on the key provided.
     */
    public MessageChannelPoolMap(Function<M, K> keyResolver, Function<K, ChannelPool> poolFactory) {
        this.keyResolver = keyResolver;
        this.poolFactory = poolFactory;
    }

    /**
     * Retrieve a {@link ChannelPool} instance using {@link AbstractMessage} as the search key.
     *
     * @param message
     *         An {@link AbstractMessage} to be used to derive the actual key from
     *
     * @return A {@link ChannelPool} instance
     */
    @Override
    public ChannelPool get(M message) {
        return internalPoolMap.get(keyResolver.apply(message));
    }

    @Override
    public boolean contains(M message) {
        return internalPoolMap.contains(keyResolver.apply(message));
    }

    @Override
    public void close() throws IOException {
        internalPoolMap.close();
    }

    @Override
    public Iterator<Map.Entry<K, ChannelPool>> iterator() {
        return internalPoolMap.iterator();
    }
}
