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
import lombok.extern.slf4j.Slf4j;
import server.netty.ChannelRepository;

@Slf4j
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
		byte [] stringMessage = (byte[]) msg;

		byte[] msgBytes = stringMessage;
		StringBuilder sb = new StringBuilder();
		for (byte b : msgBytes) {
			sb.append(String.format("%02X", b));
		}
		log.info(sb.toString());

	//	DataInBean dib = new DataInBean(sb.toString());
	//	log.info(dib.toString());
		ctx.channel().writeAndFlush(stringMessage + "\n\r");
		return;

	}

	public static byte[] toHexByte(String str, int offset, int length) {
		byte[] data = new byte[(length - offset) * 2];
		int end = offset + length;

		for (int i = offset; i < end; i++) {
			char ch = str.charAt(i);
			int high_nibble = (ch & 0xf0) >>> 4;
			int low_nibble = (ch & 0x0f);
			data[i] = (byte) high_nibble;
			data[i + 1] = (byte) low_nibble;
		}
		return data;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getMessage(), cause);
		// ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		String channelKey = ctx.channel().remoteAddress().toString();
		this.channelRepository.remove(channelKey);

		logger.debug("Binded Channel Count is " + this.channelRepository.size());
	}

	public void setChannelRepository(ChannelRepository channelRepository) {
		this.channelRepository = channelRepository;
	}
}
