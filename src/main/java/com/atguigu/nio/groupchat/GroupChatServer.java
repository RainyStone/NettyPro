package com.atguigu.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT=6667;

    public GroupChatServer(){
        try {
            selector=Selector.open();

            listenChannel=ServerSocketChannel.open();
            listenChannel.bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void listen(){
        try {
            while (true){
                int count=selector.select(2000);
                if(count>0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if(key.isAcceptable()){
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress()+" 已上线");
                        }
                        if(key.isReadable()){
                            //处理读
                            readData(key);
                        }

                        //删除处理完的key，防止重复处理
                        iterator.remove();
                    }
                }else {
                    //System.out.println("server等待client连接中......");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //读取客户端消息
    private void readData(SelectionKey key){
        SocketChannel channel=null;
        try {
            channel=(SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count=channel.read(byteBuffer);
            if(count>0){
                //在服务端显示消息
                String msg = new String(byteBuffer.array());
                System.out.println("from client("+channel.getRemoteAddress()+"): "+msg);

                //转发给其它客户端，专门写一个方法处理
                sendInfoToOtherClient(msg,channel);
            }
        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress()+" 已离线...");
                key.cancel();
                channel.close();
            }
            catch (IOException ie){
                ie.printStackTrace();
            }
        }
    }

    //转发消息给其它客户端（通道）
    private void sendInfoToOtherClient(String msg,SocketChannel self) throws IOException{
        System.out.println("服务器向其它客户端转发消息中......");
        for (SelectionKey key: selector.keys()){
            Channel targetChannel = key.channel();

            //instanceof可以排除ServerSocketChannel，后面的条件可以排除自己
            if(targetChannel instanceof SocketChannel && targetChannel!=self){
                SocketChannel dest=(SocketChannel) targetChannel;
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(byteBuffer);
            }
        }

    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
