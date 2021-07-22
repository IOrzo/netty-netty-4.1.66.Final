package com.sixtofly.study;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author xie yuan bing
 * @date 2021-07-20 14:38
 */
public class LengthFieldDecoderTest {

    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 0),
                new LoggingHandler(LogLevel.DEBUG));
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send(buffer, "hello world");
        send(buffer, "Hi!");
        channel.writeInbound(buffer);
    }


    /**
     * 发送数据
     * @param buffer
     * @param content
     */
    private static void send(ByteBuf buffer, String content) {
        byte[] bytes = content.getBytes();
        int length = bytes.length;
        buffer.writeInt(length);
        buffer.writeBytes(bytes);
    }
}
