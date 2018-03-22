package server.dataIn;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

@ChannelHandler.Sharable
public class MyByteArrayEncoder extends ByteArrayEncoder {

}