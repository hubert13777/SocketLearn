package client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

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
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            Socket client = null;
            while (true) {
                try {
                    client = new Socket(serverAddress, targetPort);
                    System.out.println();
                    break;
                } catch (ConnectException e) {
                    System.out.print("连接失败，可能是服务器未启动，按下回车重试");
                    bf.readLine();
                }
            }
            System.out.println("连接" + client.getRemoteSocketAddress() + "成功!");
            System.out.println("============================================================");
            System.out.println("                【输入\"/send_file\"传输文件】                ");
            System.out.println("                【输入\"quit\"或\"/q\"退出会话】                ");

            ClientSender sender = new ClientSender(client);
            Thread sendThread = new Thread(sender, "发送线程");
            ClientReceiver receiver = new ClientReceiver(client);
            Thread receiveThread = new Thread(receiver, "接收线程");
            sendThread.start();
            receiveThread.start();

            while (!client.isClosed()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("已关闭与" + client.getInetAddress() + "的连接");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
