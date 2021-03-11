package com.atguigu.nio;

import java.nio.ByteBuffer;

//ByteBuffer按类型put与get
public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        byteBuffer.putInt(99);
        byteBuffer.putChar('菜');
        byteBuffer.put((byte) 5);

        byteBuffer.flip();

        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.get());
    }
}
