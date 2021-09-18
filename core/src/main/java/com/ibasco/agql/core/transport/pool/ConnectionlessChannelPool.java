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
import io.netty.channel.ChannelFuture;
import io.netty.channel.pool.ChannelHealthChecker;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.SimpleChannelPool;

/**
 * A channel pool that creates connection less {@link io.netty.channel.Channel} instances
 */
public class ConnectionlessChannelPool extends SimpleChannelPool {
    public ConnectionlessChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler) {
        super(bootstrap, handler);
    }

    public ConnectionlessChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, ChannelHealthChecker healthCheck) {
        super(bootstrap, handler, healthCheck);
    }

    public ConnectionlessChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, ChannelHealthChecker healthCheck, boolean releaseHealthCheck) {
        super(bootstrap, handler, healthCheck, releaseHealthCheck);
    }

    @Override
    protected ChannelFuture connectChannel(Bootstrap bs) {
        return bs.localAddress(0).bind();
    }
}
