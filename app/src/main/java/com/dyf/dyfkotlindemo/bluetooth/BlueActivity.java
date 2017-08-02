package com.dyf.dyfkotlindemo.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.dyf.dyfkotlindemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dyf on 2017/8/1.
 */

public class BlueActivity extends AppCompatActivity {
    public static final String TAG = BlueActivity.class.getSimpleName();
    public static final int REQUEST_COARSE_LOCATION_PERMISSIONS = 10010;

    Button find_device_btn;
    ListView device_list;

    //
    private BlueAdapter mBlueAdapter;
    private BleManager bleManager;

    //搜索到的远程设备集合
    private List<BluetoothDevice> discoveredDevices = new ArrayList<>();

    //蓝牙适配器
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    //蓝牙搜索广播的接收器
    private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //获取广播的Action
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //开始搜索
                Log.d("bluetooth" , "search begin");
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //发现远程蓝牙设备
                //获取设备
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                discoveredDevices.add(bluetoothDevice);

//                //发送发现设备广播
//                Intent deviceListIntent = new Intent(BluetoothTools.ACTION_FOUND_DEVICE);
//                deviceListIntent.putExtra(BluetoothTools.DEVICE, bluetoothDevice);
//                sendBroadcast(deviceListIntent);
//                //.........
//                // 将设备名称和地址放入array adapter，以便在ListView中显示
//                mArrayAdapter.add(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress());
                Log.d("bluetooth" , bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress());
                mBlueAdapter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //搜索结束
                Log.d("bluetooth" , "search end");
                if (discoveredDevices.isEmpty()) {
                    //若未找到设备，则发动未发现设备广播
//                    Intent foundIntent = new Intent(BluetoothTools.ACTION_NOT_FOUND_SERVER);
//                    sendBroadcast(foundIntent);
                    Log.d("bluetooth" , "no bluetooth device");
                }
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        find_device_btn = (Button) findViewById(R.id.find_device_btn);
        device_list = (ListView) findViewById(R.id.device_list);

        mBlueAdapter = new BlueAdapter(BlueActivity.this, discoveredDevices);
        device_list.setAdapter(mBlueAdapter);
        initBluetooth();

        find_device_btn.setOnClickListener(v -> {
            //开始搜索
            doDiscovery();
        });

        device_list.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice device = discoveredDevices.get(position);
            //开启设备连接线程
            new BluetoothClientConnThread(handler, device).start();
        });
    }

    //接收其他线程消息的Handler
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //处理消息
            switch (msg.what) {
                case BluetoothTools.MESSAGE_CONNECT_ERROR:
                    //连接错误
//                    //发送连接错误广播
//                    Intent errorIntent = new Intent(BluetoothTools.ACTION_CONNECT_ERROR);
//                    sendBroadcast(errorIntent);
                    Log.d("bluetooth" , "bluetooth connect error");
                    Toast.makeText(BlueActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothTools.MESSAGE_CONNECT_SUCCESS:
                    //连接成功

                    //开启通讯线程
//                    communThread = new BluetoothCommunThread(handler, (BluetoothSocket)msg.obj);
//                    communThread.start();

//                    //发送连接成功广播
//                    Intent succIntent = new Intent(BluetoothTools.ACTION_CONNECT_SUCCESS);
//                    sendBroadcast(succIntent);
                    Log.d("bluetooth" , "bluetooth connect success");
                    Toast.makeText(BlueActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothTools.MESSAGE_READ_OBJECT:
                    //读取到对象
//                    //发送数据广播（包含数据对象）
//                    Intent dataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_GAME);
//                    dataIntent.putExtra(BluetoothTools.DATA, (Serializable)msg.obj);
//                    sendBroadcast(dataIntent);
                    break;
            }
            super.handleMessage(msg);
        }

    };


    public void doDiscovery() {
        int hasPermission = ActivityCompat.checkSelfPermission(BlueActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            continueDoDiscovery();
            return;
        }

        ActivityCompat.requestPermissions(BlueActivity.this,
                new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_COARSE_LOCATION_PERMISSIONS);
    }

    private void continueDoDiscovery(){
        discoveredDevices.clear();	//清空存放设备的集合

        if (bluetoothAdapter == null) {
            // 设备不支持蓝牙
            return;
        }
        // 打开蓝牙
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 设置蓝牙可见性，最多300秒
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
        }else{
            bluetoothAdapter.enable();	//打开蓝牙
            bluetoothAdapter.startDiscovery();	//开始搜索
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION_PERMISSIONS: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    continueDoDiscovery();
                } else {
                    Toast.makeText(this,getResources().getString(R.string.permission_failure),
                            Toast.LENGTH_LONG).show();
//                    cancelOperation();
                }
                return;
            }
        }
    }

    private void initBluetooth(){
        //discoveryReceiver的IntentFilter
        IntentFilter discoveryFilter = new IntentFilter();
        discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        discoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);


        //注册BroadcastReceiver
        registerReceiver(discoveryReceiver, discoveryFilter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(discoveryReceiver);
        super.onDestroy();
    }
}
