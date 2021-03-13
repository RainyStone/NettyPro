package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    //当通道就绪时会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client--->"+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello server,this is client", CharsetUtil.UTF_8));
    }

    //当通道有读取事件时，会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //将msg转成ByteBuf，注意：ByteBuf是Netty提供的，不是NIO的ByteBuffer
        ByteBuf buf=(ByteBuf) msg;
        System.out.println("the relpy from server: "+buf.toString(CharsetUtil.UTF_8));
        System.out.println("the address of server: "+ctx.channel().remoteAddress());
    }

    //异常处理，一般是需要在这里关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("发生异常");
        cause.printStackTrace();
        ctx.close();
    }
}
