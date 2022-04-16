package client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientReceiver implements Runnable {
    Socket client;
    private final String filePath = "./client/recv_file/";

    public ClientReceiver(Socket socket) {
        client = socket;
        // filePath目录不存在 则创建
        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                System.out.println("创建./rece_file目录失败!!!");
            }
        }
    }

    private void receiveFile(String filename) {
        System.out.println("对方(Server)尝试向您传输文件[" + filename + "], 是否接收?(Y/N)");
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        try {
            String str = bf.readLine();
            if (str.equals("Y") || str.equals("") || str.equals("y")) {
                System.out.println("正在接收文件" + filename + "...");
            } else {
                System.out.println("用户拒绝接收文件");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        File file = new File(filePath + filename);

        try {
            FileOutputStream f = new FileOutputStream(file);
            BufferedOutputStream fout = new BufferedOutputStream(f);
            DataInputStream din = new DataInputStream(client.getInputStream());
            byte[] bytes = new byte[1024];
            int length;
            while ((length = din.read(bytes, 0, bytes.length)) != -1) {
                fout.write(bytes, 0, length);
                fout.flush();
            }
            fout.close();
            System.out.println("文件接收成功!路径为 " + file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
                        receiveFile(in.readUTF());
                        continue;
                    }

                    char[] input = str.toCharArray();
                    System.out.println("对方(Server): ");
                    int length = input.length;
                    for (int i = 1; i <= length; i++) {
                        System.out.print(input[i - 1]);
                        if (i % 30 == 0) System.out.println();
                    }
                    System.out.println();
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
