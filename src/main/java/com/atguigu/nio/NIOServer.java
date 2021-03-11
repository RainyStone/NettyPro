package com.atguigu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(6666));

        Selector selector = Selector.open();

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            //等待1秒，如果为0，即暂无关注的事件发生，continue继续等待
            if(selector.select(1000)==0){
                System.out.println("服务器等待1秒，无连接");
                continue;
            }
            else {//不为0，即>0，说明有关注的事件发生，进行处理
                //获取已发生关注事件的selectionKey
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();

                    if(key.isAcceptable()){
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        System.out.println("客户端连接");
                        socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    }

                    if(key.isReadable()){
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        channel.read(buffer);
                        System.out.println("from client--->"+new String(buffer.array()));
                    }

                    keyIterator.remove();
                }

            }
        }
    }
}
