package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
                Socket server = listener.accept();
                System.out.println("连接成功, 远程主机地址为: " + server.getRemoteSocketAddress());
                DataInputStream in=new DataInputStream(server.getInputStream());
                DataOutputStream out=new DataOutputStream(server.getOutputStream());

                String receive=in.readUTF();
                System.out.println("客户端: "+receive);
                out.writeUTF("Hi ( ´･･)ﾉ(._.`)");
                while (true){

                }
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
