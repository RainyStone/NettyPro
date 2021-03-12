package com.atguigu.nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",7001));
        String filename="src/main/java/com/atguigu/testfile/niofilechannel01.txt";
        FileChannel fileChannel = new FileInputStream(filename).getChannel();

        long startTime=System.currentTimeMillis();

        //transferTo方法底层使用零拷贝
        //linux系统下，transferTo方法可以一次性传输文件
        //windows系统下，transferTo方法一次最多只能发送8m，大文件的话就需要分段传输
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送总字节数："+transferCount+"，耗时："+(System.currentTimeMillis()-startTime));

        fileChannel.close();

        socketChannel.close();
    }
}
