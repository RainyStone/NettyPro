package com.atguigu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

//Buffer的分散与聚集
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        serverSocketChannel.bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        System.out.println("服务端启动----");
        SocketChannel socketChannel=serverSocketChannel.accept();
        int messageLength=8;
        while (true){
            int byteRead=0;
            while (byteRead<messageLength){
                long l=socketChannel.read(byteBuffers);
                byteRead+=l;
                System.out.println("byteRead="+byteRead);
                Arrays.asList(byteBuffers).stream().map(byteBuffer -> "position="+byteBuffer.position()+",limit="+byteBuffer.limit()).forEach(System.out::println);
            }

            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

            long byteWrite=0;
            while (byteWrite<messageLength){
                long l=socketChannel.write(byteBuffers);
                byteWrite+=1;
            }

            Arrays.asList(byteBuffers).forEach(ByteBuffer::clear);

            System.out.println("byteRead:="+byteRead+" byteWrite:="+byteWrite+" messageLength:="+messageLength);
        }
    }
}
