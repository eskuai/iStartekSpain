package server.dataIn;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.bytes.ByteArrayDecoder;

@ChannelHandler.Sharable
public class MyByteArrayDecoder extends ByteArrayDecoder {

}