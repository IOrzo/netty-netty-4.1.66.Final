/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // discard
        // 1. Discard the received data silently(默默地).
//        ((ByteBuf) msg).release();

        // 2. Do something with msg
//        try {
//            // Do something with msg
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }

        // 3. 打印出来
        ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) { // (1)
                System.out.print((char) in.readByte());
                System.out.flush();
            }
            System.out.println("读取完毕");
        } finally {
            /**
             * error: io.netty.util.IllegalReferenceCountException: refCnt: 0, decrement: 1
             *
             * SimpleChannelInboundHandler会自动释放内存（虽然这是一种软释放）即是refCnt引用数减一。
             * 而本人在使用SimpleChannelInboundHandler作为Server端的时候，自己手动释放了一次msg的内存，
             * 导致refCnt引用数为0，这个时候框架试图去释放一次，就报如上错误
             */

            ReferenceCountUtil.release(msg); // (2)
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
