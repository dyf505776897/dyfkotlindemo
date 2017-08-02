package com.dyf.dyfkotlindemo.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dyf.dyfkotlindemo.R;

import java.util.List;

/**
 * Created by dyf on 2017/8/1.
 */

public class BlueAdapter extends BaseAdapter {


    private Context mContext;
    private List<BluetoothDevice> discoveredDevices;

    public BlueAdapter(Context context, List<BluetoothDevice> datas){
        this.mContext = context;
        this.discoveredDevices = datas;
    }

    @Override
    public int getCount() {
        return discoveredDevices.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return discoveredDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder mHolder;
        if (view == null) {
            mHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.blue_item, null);

            mHolder.title_text = (TextView) view.findViewById(R.id.title_text);
            mHolder.address_text = (TextView) view.findViewById(R.id.address_text);
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        mHolder.address_text.setText(discoveredDevices.get(position).getAddress());
        mHolder.title_text.setText(discoveredDevices.get(position).getName());

        return view;
    }


    public void add(BluetoothDevice discoveredDevice){
        discoveredDevices.add(discoveredDevice);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView title_text, address_text;
    }





}
