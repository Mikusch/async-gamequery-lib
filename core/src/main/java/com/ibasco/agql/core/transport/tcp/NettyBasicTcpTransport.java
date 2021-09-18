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

package com.ibasco.agql.core.transport.tcp;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.enums.ChannelType;
import com.ibasco.agql.core.transport.NettyTransport;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * <p>A basic TCP Transport Implementation which creates a new TCP Channel per connection</p>
 *
 * @param <M> A request extending {@link AbstractRequest}
 */
public class NettyBasicTcpTransport<M extends AbstractRequest> extends NettyTransport<M> {

    private static final Logger log = LoggerFactory.getLogger(NettyBasicTcpTransport.class);

    public NettyBasicTcpTransport(ChannelType channelType) {
        super(channelType);
        NettyTransport transport = this;
        getBootstrap().handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                getChannelInitializer().initializeChannel(ch, transport);
            }
        });
    }

    @Override
    public CompletableFuture<Channel> getChannel(M address) {
        final CompletableFuture<Channel> channelFuture = new CompletableFuture<>();
        ChannelFuture f = getBootstrap().connect(address.recipient());
        //Acquire from pool and listen for completion
        f.addListener((ChannelFuture future) -> {
            if (future.isSuccess()) {
                channelFuture.complete(future.channel());
            } else {
                channelFuture.completeExceptionally(future.cause());
            }
        });
        return channelFuture;
    }
}
