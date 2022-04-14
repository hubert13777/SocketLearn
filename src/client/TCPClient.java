package client;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

public class TCPClient {
    public static void main(String[] args) {
        int targetPort = 50001;
        String serverAddress = "127.0.0.1";
        int argsLen = args.length;
        for (int i = 0; i < argsLen - 1; i += 2) {
            if (args[i].equals("--port")) {
                try {
                    targetPort = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    System.out.println("please enter the correct number of port!");
                    System.exit(-1);
                }
            } else if (args[i].equals("--ip")) {

            }
        }
        System.out.println("启动客户端...");
        try {
            System.out.println("正在尝试连接到主机 " + serverAddress + ":" + targetPort + "...");
            Socket client = new Socket(serverAddress, targetPort);
            System.out.println("连接" + client.getRemoteSocketAddress() + "成功!");

            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());

            out.writeUTF("Hello ヾ(•ω•`)o");
            String received = in.readUTF();
            System.out.println("服务器响应: " + received);
//            while(true){
//
//            }
            client.close();
            System.out.println("已关闭与" + client.getInetAddress() + "的连接");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
