package com.dyf.dyfkotlindemo.ipcdemo.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dyf.dyfkotlindemo.utils.MyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by dyf on 2017/6/20.
 * Socket 实现进程间的通讯
 */

public class TCPServerService extends Service {

    private boolean mIsServiceDestoryed = false;
    private String[] mDefinedMessage = new String[] {
            "你好啊,哈哈",
            "请问你叫什么名字呀",
            "今天杭州天气不错啊,shy",
            "你知道吗?我可是可以和多个人同时聊天哦"
    };

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        mIsServiceDestoryed = true;
        super.onDestroy();
    }


    private class TcpServer implements Runnable{

        @Override
        public void run() {

            ServerSocket serverSocket = null;
            //监听本地端口8688
            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                System.out.println("establish tcp server failed,port :8688");
                e.printStackTrace();
                return;
            }


            while (!mIsServiceDestoryed){
                //接受客户端的请求
                try {
                    final Socket client = serverSocket.accept();
                    System.out.println("accept");
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException {
        //用于接收客户端的消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        //用于向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("欢迎来到聊天室!");

        while (!mIsServiceDestoryed){
            String str = in.readLine();
            System.out.println("msg from client : " + str);
            if (null == str){
                //客户端断开连接
                break;
            }
            int i = new Random().nextInt(mDefinedMessage.length);
            String msg = mDefinedMessage[i];
            out.println(msg);
            System.out.println("send : " + msg);
        }
        System.out.println("client quit .");
        //关闭流
        MyUtils.close(out);
        MyUtils.close(in);
        client.close();
    }




}
