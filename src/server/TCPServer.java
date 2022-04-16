package server;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServer {
    public static void main(String[] args) {
        int localPort = 50001;
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(localPort);
            System.out.println("客户端启动成功, 本地端口为 " + localPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("绑定端口失败，程序终止");
            System.exit(-1);
        }
        while (true) {
            System.out.println("等待连接中...");
            try {
                ServerSocketThread server=new ServerSocketThread(listener.accept());
                server.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
