package client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientReceiver implements Runnable {
    Socket client;
    private final String filePath = "recv_file/client/";

    public ClientReceiver(Socket socket) {
        client = socket;
        // filePath目录不存在 则创建
        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                System.out.println("创建" + filePath + "目录失败!!!");
            }
        }
    }

    private void receiveFile(String filename, long fileSize) {
        System.out.println("对方(Server)尝试向您传输文件[" + filename + "], 文件大小为" + fileSize + "Byte");
        System.out.println("正在接收文件" + filename + "...");

        File file = new File(filePath + filename);

        try {
            FileOutputStream f = new FileOutputStream(file);
            BufferedOutputStream fout = new BufferedOutputStream(f);
            DataInputStream din = new DataInputStream(client.getInputStream());
            byte[] bytes = new byte[1024];
            int length = 0;  // 文件长度
            while (length < fileSize) {
                length += din.read(bytes, 0, bytes.length);
                fout.write(bytes, 0, (length-1)%1024+1);
                fout.flush();
            }
            fout.close();
            System.out.println("文件接收成功!路径为 " + file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件读取出错");
        }
    }

    @Override
    public void run() {
        DataInputStream in;
        try {
            in = new DataInputStream(client.getInputStream());
            System.out.println("============================================================");

            while (true) {
                try {
                    String str = in.readUTF();
                    if (str.equals("--QUIT")) break;
                    else if (str.equals("--FILE")) {
                        String[] file = in.readUTF().split("\\?", 2);
                        receiveFile(file[0], Long.parseLong(file[1]));
                    }else {
                        char[] input = str.toCharArray();
                        System.out.println("对方(Server): ");
                        int length = input.length;
                        for (int i = 1; i <= length; i++) {
                            System.out.print(input[i - 1]);
                            if (i % 30 == 0) System.out.println();
                        }
                        System.out.println();
                    }
                } catch (SocketException e) {
                    break;
                }
                System.out.println("============================================================");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
