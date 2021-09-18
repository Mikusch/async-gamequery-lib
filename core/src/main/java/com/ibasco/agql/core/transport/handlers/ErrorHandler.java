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

package com.ibasco.agql.core.transport.handlers;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.exceptions.ResponseException;
import com.ibasco.agql.core.exceptions.TransportException;
import com.ibasco.agql.core.transport.ChannelAttributes;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler extends ChannelDuplexHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (log.isDebugEnabled()) {
            log.error("Unhandled exception caught within the pipeline {} for Channel {}, Id: {}", cause, ctx.channel(), ctx.channel().id());
            if (ctx.channel().hasAttr(ChannelAttributes.LAST_REQUEST_SENT)) {
                AbstractRequest request = ctx.channel().attr(ChannelAttributes.LAST_REQUEST_SENT).get();
                if (request != null && SocketChannel.class.isAssignableFrom(ctx.channel().getClass())) {
                    Throwable ex = new ResponseException(request, cause);
                    SimpleChannelInboundHandler responseRouter = ctx.pipeline().get(SimpleChannelInboundHandler.class);
                    responseRouter.channelRead(ctx, ex);
                    return;
                }
            }
            throw new TransportException(cause);
        }
    }
}
