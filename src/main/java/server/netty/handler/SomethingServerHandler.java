/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.netty.handler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import server.netty.ChannelRepository;


@Component
@Qualifier("somethingServerHandler")
@ChannelHandler.Sharable
public class SomethingServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ChannelRepository channelRepository;

    private static Logger logger = Logger.getLogger(SomethingServerHandler.class.getName());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
        logger.debug(ctx.channel().remoteAddress());
        String channelKey = ctx.channel().remoteAddress().toString();
        channelRepository.put(channelKey, ctx.channel());
        ctx.writeAndFlush("Your channel key is " + channelKey + "\n\r");
        logger.debug("Binded Channel Count is " + this.channelRepository.size());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String stringMessage = (String) msg;

        logger.info("mierda:="+stringMessage);

        String[] splitMessage = stringMessage.split("::");

        if ( splitMessage.length != 2 ) {
            ctx.channel().writeAndFlush(stringMessage + "\n\r");
            return;
        }

        if ( channelRepository.get(splitMessage[0]) != null ) {
            channelRepository.get(splitMessage[0]).writeAndFlush(splitMessage[1] + "\n\r");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(), cause);
        //ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        String channelKey = ctx.channel().remoteAddress().toString();
        this.channelRepository.remove(channelKey);

        logger.debug("Binded Channel Count is " + this.channelRepository.size());
    }

    public void setChannelRepository(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }
}
