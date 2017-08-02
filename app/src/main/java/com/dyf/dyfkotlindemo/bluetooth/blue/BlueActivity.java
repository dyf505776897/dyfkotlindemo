package com.dyf.dyfkotlindemo.bluetooth.blue;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.conn.BleGattCallback;
import com.clj.fastble.data.ScanResult;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.dyf.dyfkotlindemo.R;
import com.dyf.dyfkotlindemo.bluetooth.BlueAdapter;
import com.dyf.dyfkotlindemo.bluetooth.BluetoothClientConnThread;
import com.dyf.dyfkotlindemo.bluetooth.BluetoothTools;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by dyf on 2017/8/1.
 */

public class BlueActivity extends AppCompatActivity {
    public static final String TAG = BlueActivity.class.getSimpleName();
    public static final int REQUEST_COARSE_LOCATION_PERMISSIONS = 10010;

    Button find_device_btn;
    Button disconnect;
    Button get_device_info;
    ListView device_list;
    TextView current_info;

    //
    private BlueAdapter mBlueAdapter;
    private String mac;
    private BluetoothService mBluetoothService;
    private ProgressDialog progressDialog;

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
        disconnect = (Button) findViewById(R.id.disconnect);
        get_device_info = (Button) findViewById(R.id.get_device_info);
        current_info = (TextView) findViewById(R.id.current_info);
        device_list = (ListView) findViewById(R.id.device_list);

        mBlueAdapter = new BlueAdapter(BlueActivity.this, discoveredDevices);
        device_list.setAdapter(mBlueAdapter);
        initBluetooth();


        progressDialog = new ProgressDialog(this);

        find_device_btn.setOnClickListener(v -> {
            //开始搜索
            doDiscovery();
        });

        device_list.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice device = discoveredDevices.get(position);
            //开启设备连接线程
            mac = device.getAddress();
            if (mBluetoothService == null) {
                bindService();
            } else {
                mBluetoothService.scanAndConnect5(mac);
            }
        });

        disconnect.setOnClickListener(v -> {
            if (mBluetoothService != null) {
                mBluetoothService.disConnect();
            }
        });

        //
        get_device_info.setOnClickListener(v -> {
            getDeviceInfo();
        });
    }

    private void bindService() {
        Intent bindIntent = new Intent(this, BluetoothService.class);
        this.bindService(bindIntent, mFhrSCon, Context.BIND_AUTO_CREATE);
    }

    private void unbindService() {
        this.unbindService(mFhrSCon);
    }

    private ServiceConnection mFhrSCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothService = ((BluetoothService.BluetoothBinder) service).getService();
            mBluetoothService.setScanCallback(callback);
            mBluetoothService.scanAndConnect5(mac);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothService = null;
        }
    };

    private BluetoothService.Callback callback = new BluetoothService.Callback() {
        @Override
        public void onStartScan() {
//            img_loading.startAnimation(operatingAnim);
            current_info.setText("当前连接: " );
        }

        @Override
        public void onScanning(ScanResult result) {

        }

        @Override
        public void onScanComplete() {
//            img_loading.clearAnimation();
        }

        @Override
        public void onConnecting() {
            progressDialog.show();
        }

        @Override
        public void onConnectFail() {
//            img_loading.clearAnimation();
            progressDialog.dismiss();
            Toast.makeText(BlueActivity.this, "连接失败", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDisConnected() {
//            img_loading.clearAnimation();
            progressDialog.dismiss();
            Toast.makeText(BlueActivity.this, "连接断开", Toast.LENGTH_LONG).show();
            current_info.setText("当前连接: " );
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == 0) {
                BluetoothGattCharacteristic localBluetoothGattCharacteristic = gatt.getService(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
                if (localBluetoothGattCharacteristic != null) {
                    Log.w("TAG", "成功获取特征");
                    gatt.setCharacteristicNotification(localBluetoothGattCharacteristic, true);
                }
            }

            progressDialog.dismiss();
//            startActivity(new Intent(BlueActivity.this, OperationActivity.class));
            Toast.makeText(BlueActivity.this, "连接成功", Toast.LENGTH_LONG).show();
            current_info.setText("当前连接: " + mac);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (characteristic.getValue().length != 0) {
                String str = HexUtil.encodeHexStr(characteristic.getValue());
                Toast.makeText(BlueActivity.this, str, Toast.LENGTH_LONG).show();
                Log.w("TAG", str);
            }
        }
    };

    private void openLock(){
        sendCmd("6378646342000000010000000131353030303834", new BleCharacterCallback(){

            @Override
            public void onFailure(BleException exception) {
                Log.d("bluetooth" , "bluetooth onFailure1 error");
            }

            @Override
            public void onInitiatedResult(boolean result) {
                Log.d("bluetooth" , "bluetooth onInitiatedResult1 error");
            }

            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                sendCmd("3331383600036166613939393761636161373866", new BleCharacterCallback(){

                    @Override
                    public void onFailure(BleException exception) {
                        Log.d("bluetooth" , "bluetooth onFailure2 error");
                    }

                    @Override
                    public void onInitiatedResult(boolean result) {
                        Log.d("bluetooth" , "bluetooth onInitiatedResult2 error");
                    }

                    @Override
                    public void onSuccess(BluetoothGattCharacteristic characteristic) {
                        sendCmd("3030383233383733366633323433313461616636", new BleCharacterCallback(){

                            @Override
                            public void onFailure(BleException exception) {
                                Log.d("bluetooth" , "bluetooth onFailure3 error");
                            }

                            @Override
                            public void onInitiatedResult(boolean result) {
                                Log.d("bluetooth" , "bluetooth onInitiatedResult3 error");
                            }

                            @Override
                            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                                sendCmd("663832613733b969", new BleCharacterCallback(){

                                    @Override
                                    public void onFailure(BleException exception) {
                                        Log.d("bluetooth" , "bluetooth onFailure4 error");
                                    }

                                    @Override
                                    public void onInitiatedResult(boolean result) {
                                        Log.d("bluetooth" , "bluetooth onInitiatedResult4 error");
                                    }

                                    @Override
                                    public void onSuccess(BluetoothGattCharacteristic characteristic) {
                                        runOnUiThread(() -> Toast.makeText(BlueActivity.this, "发送成功", Toast.LENGTH_LONG).show());

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public void sendCmd(final String paramString, final BleCharacterCallback paramBleCharacterCallback) {
        if (null != mBluetoothService && mBluetoothService.isConnected()){
            runOnUiThread(() -> mBluetoothService.write("0000ffe0-0000-1000-8000-00805f9b34fb", "0000ffe1-0000-1000-8000-00805f9b34fb", paramString, paramBleCharacterCallback));
        }else{
            Toast.makeText(BlueActivity.this, "请先连接设备", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 获取板子信息
     */
    public void getDeviceInfo(){
        sendCmd("637864630c000000040000000114", new BleCharacterCallback() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                runOnUiThread(() -> Toast.makeText(BlueActivity.this, "发送成功", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(BleException exception) {

            }

            @Override
            public void onInitiatedResult(boolean result) {

            }
        });
    }

    public void doDiscovery() {
        int hasPermission = ActivityCompat.checkSelfPermission(BlueActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            continueDoDiscovery();
            return;
        }

        ActivityCompat.requestPermissions(BlueActivity.this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION},
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
        if (mBluetoothService != null)
            unbindService();
    }
}
