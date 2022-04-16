package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerSocketThread implements Runnable{
    private Socket server;
    public ServerSocketThread(Socket socket){
        server=socket;
        System.out.println("连接成功, 远程主机地址为: " + server.getRemoteSocketAddress());
        System.out.println("============================================================");
        System.out.println("                【输入\"quit\"或\"/q\"退出会话】                ");
        System.out.println("============================================================");
    }

    @Override
    public void run() {

        try{
        DataInputStream in=new DataInputStream(server.getInputStream());
        DataOutputStream out=new DataOutputStream(server.getOutputStream());

        ServerSender sender=new ServerSender(server);
        Thread sendThread=new Thread(sender,"发送线程");
        ServerReceiver receiver=new ServerReceiver(server);
        Thread receiveThread=new Thread(receiver,"接收线程");
        receiveThread.start();
        sendThread.start();

        while(!server.isClosed()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("客户端"+server.getInetAddress()+"已断开连接\n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
