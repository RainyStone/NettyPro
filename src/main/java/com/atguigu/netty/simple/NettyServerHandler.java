package com.atguigu.netty.simple;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

//自定义Handler，需要继承Netty规定好的某个HandlerAdapter
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /*读取数据时调用
    1、ChannelHandlerContext ctx：上下文对象，其中含有 通道channel、管道pipeline、地址 等信息
    2、Object msg：客户端发送的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx="+ctx);
        //将msg转成ByteBuf，注意：ByteBuf是Netty提供的，不是NIO的ByteBuffer
        ByteBuf buf=(ByteBuf) msg;
        System.out.println("the message from client: "+buf.toString(CharsetUtil.UTF_8));
        System.out.println("the address of client: "+ctx.channel().remoteAddress());
    }

    //数据读取完毕时调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,this is the reply from server",CharsetUtil.UTF_8));
    }

    //异常处理，一般是需要在这里关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
