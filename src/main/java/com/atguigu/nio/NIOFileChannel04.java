package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("src/main/java/com/atguigu/testfile/src.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("src/main/java/com/atguigu/testfile/dest.jpg");

        FileChannel srcChannel = fileInputStream.getChannel();
        FileChannel ouputChannel = fileOutputStream.getChannel();

        ouputChannel.transferFrom(srcChannel,0,srcChannel.size());

        fileInputStream.close();
        fileOutputStream.close();
    }
}
