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

package com.ibasco.agql.core.transport.udp;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.enums.ChannelType;
import com.ibasco.agql.core.transport.NettyTransport;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class NettyBasicUdpTransport<M extends AbstractRequest> extends NettyTransport<M> {

    private Logger log = LoggerFactory.getLogger(NettyBasicUdpTransport.class);
    private NioDatagramChannel channel; //maintain only one channel

    public NettyBasicUdpTransport(ChannelType channelType) {
        super(channelType);
        NettyTransport transport = this;
        getBootstrap().handler(new ChannelInitializer<NioDatagramChannel>() {
            @Override
            protected void initChannel(NioDatagramChannel ch) throws Exception {
                getChannelInitializer().initializeChannel(ch, transport);
            }
        });
    }

    @Override
    public CompletableFuture<Channel> getChannel(M message) {
        final CompletableFuture<Channel> cf = new CompletableFuture<>();
        //lazy initialization
        if (channel == null || !channel.isOpen()) {
            bind(0).addListener((ChannelFuture future) -> {
                if (future.isSuccess()) {
                    channel = (NioDatagramChannel) future.channel();
                    channel.closeFuture().addListener((ChannelFuture f) -> log.debug("CHANNEL CLOSED: {}, Is Open: {}, For Address: {}, Cause: {}", f.channel().id(), f.channel().isOpen(), message.recipient(), f.cause()));
                    cf.complete(channel);
                } else {
                    channel = null;
                    cf.completeExceptionally(future.cause());
                }
            });
        } else {
            cf.complete(channel);
        }
        return cf;
    }
}
