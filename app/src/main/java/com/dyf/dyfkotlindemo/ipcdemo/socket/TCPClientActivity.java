package com.dyf.dyfkotlindemo.ipcdemo.socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dyf.dyfkotlindemo.R;
import com.dyf.dyfkotlindemo.utils.MyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;

/**
 * Created by dyf on 2017/6/20.
 */

public class TCPClientActivity extends Activity implements View.OnClickListener {


    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECTED = 2;

    private Button mSendButton;
    private TextView mMessageTextView;
    private EditText mMessageEditText;

    private PrintWriter mPrintWriter;
    private Socket mClientSocket;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_RECEIVE_NEW_MSG : {
                    mMessageTextView.setText(mMessageTextView.getText() + (String)msg.obj);
                    break;
                }
                case MESSAGE_SOCKET_CONNECTED : {
                    mSendButton.setEnabled(true);
                }
                default:
                    break;
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient);

        mMessageTextView = (TextView) findViewById(R.id.msg_container);
        mSendButton = (Button) findViewById(R.id.send);
        mMessageEditText = (EditText) findViewById(R.id.msg);
        mSendButton.setOnClickListener(this);

        Intent service = new Intent(this, TCPServerService.class);
        startService(service);

        new Thread(){
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();
    }


    @Override
    protected void onDestroy() {
        if (null !=  mClientSocket) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == mSendButton) {
            final String msg = mMessageEditText.getText().toString();
            if (!TextUtils.isEmpty(msg) && null != mPrintWriter){
                new Thread(){
                    @Override
                    public void run() {
                        mPrintWriter.println(msg);
                    }
                }.start();

                mMessageEditText.setText("");
                String time = formatDateTime(System.currentTimeMillis());
                final String showedMsg = "self " + time + " : " + msg + "\n";
                mMessageTextView.setText(mMessageTextView.getText() + showedMsg);
            }

        }

    }


    private String formatDateTime(long time){
        return new SimpleDateFormat("(HH:mm:ss)").format(time);
    }


    private void connectTCPServer(){
        Socket socket = null;
        while (socket == null){
            try {
                socket = new Socket("localhost", 8688);
                mClientSocket = socket;
                // autoFlush
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                System.out.println("connect server success");
            } catch (IOException e) {
                SystemClock.sleep(1000);
//                e.printStackTrace();
                System.out.println("connect tcp server failed, retry ...");
            }
        }

        //接收服务器端的消息
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!TCPClientActivity.this.isFinishing()){
                String msg = br.readLine();
                System.out.println("receive : " + msg);
                if (null != msg){
                    String time = formatDateTime(System.currentTimeMillis());
                    final String showMsg = "server : " + time + " : " + msg + "\n";
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showMsg).sendToTarget();
                }
            }
            // quit
            System.out.println("quit ... ");
            MyUtils.close(mPrintWriter);
            MyUtils.close(br);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
