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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.pool.*;

/**
 * A default {@link ChannelPoolMap} implementation that use a {@link SimpleChannelPool}
 */
public class DefaultChannelPoolMap<K, P extends ChannelPool> extends AbstractChannelPoolMap<K, P> {

    private Bootstrap bootstrap;
    private ChannelPoolHandler handler;

    public DefaultChannelPoolMap(Bootstrap bootstrap, ChannelPoolHandler handler) {
        this.bootstrap = bootstrap;
        this.handler = handler;
    }

    @Override
    protected P newPool(K key) {
        return (P) new SimpleChannelPool(bootstrap, handler);
    }
}
