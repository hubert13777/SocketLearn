package client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientSender implements Runnable {
    Socket client;

    public ClientSender(Socket socket) {
        client = socket;
    }

    private void sendFile() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("/**************准备发送文件, 输入\"/q\"可终止**************/");
        System.out.println("/***************请输入文件名(包括绝对路径)***************/");

        DataOutputStream dout = null;
        try {
            dout = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = null;
        try {
            while (true) {
                String filename = bf.readLine();
                if (filename.equals("/q")) {
                    System.out.println("用户终止发送");
                    return;
                } else if (filename.equals("")) {
                    continue;
                } else if (filename.charAt(filename.length() - 1) == '/' ||
                        filename.charAt(filename.length() - 1) == '\\') {
                    System.out.println("/***************请输入文件名, 而不是目录名****************/");
                    continue;
                }

                file = new File(filename);
                if (!file.exists()) {
                    System.out.println("/****************该文件不存在，请重新输入!****************/");
                } else {
                    StringBuilder temp = new StringBuilder();
                    // 发送不包括路径名的文件名
                    for (int i = filename.length() - 1; i >= 0; i--) {
                        if (filename.charAt(i) == '/' || filename.charAt(i) == '\\') break;
                        temp.insert(0, filename.charAt(i));
                    }
                    String fileSize = String.valueOf(file.length());
                    // ?后跟文件大小
                    dout.writeUTF(temp.toString() + "?" + fileSize);
                    dout.flush();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            FileInputStream f = new FileInputStream(file);
            BufferedInputStream fin = new BufferedInputStream(f);

            byte[] bytes = new byte[1024];
            int length = 0;
            long size = file.length();
            while (length < size) {
                length += fin.read(bytes, 0, bytes.length);
                dout.write(bytes, 0, (length-1)%bytes.length+1);
                dout.flush();
            }
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(client.getOutputStream());
            out.writeUTF("Hello ヾ(•ω•`)o");
            while (!client.isClosed()) {
                String input = bf.readLine();
                if (input.equals("/q") || input.equals("quit")) { //需要退出客户端
                    out.writeUTF("--QUIT");
                    out.flush();
                    break;
                } else if (input.equals("/send_file")) {
                    out.writeUTF("--FILE");
                    out.flush();
                    sendFile();
                } else {
                    out.writeUTF(input);
                }
                System.out.println("============================================================");
            }
            client.close();
        } catch (SocketException e) {
            System.out.println("Socket已中断，请重新连接");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
