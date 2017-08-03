package com.dyf.dyfkotlindemo.saolei.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.dyf.dyfkotlindemo.R;
import com.dyf.dyfkotlindemo.saolei.entity.ItemEntity;
import com.dyf.dyfkotlindemo.saolei.entity.QiPanEntity;


/**填充GridView的Adapter，这里主要用来控制显示和传递逻辑*/
public class LeiAdapter extends BaseAdapter {

    private int level;
    private QiPanEntity entity;
    private Context context;
    private GridView gv;

    public LeiAdapter(int level,Context context,GridView gv) {
        this.level = level;
        entity = new QiPanEntity(level);
        this.context = context;
        this.gv = gv;
    }
    @Override
    public int getCount() {
        return level*level;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_img, null);
        }
        ((ImageView)convertView).setImageResource(getRes(getItem(position)));
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                gv.getWidth()/level);   //用传递进来的GridView计算宽度，并设置为单元格高度，保证棋盘是正方形
        convertView.setLayoutParams(param);
        return convertView;
    }

    /**根据单元格状态生成不同的图片*/
    private int getRes(ItemEntity entity){
        int id = 0;
        if(entity.isBiaoJi() && !entity.isBiaoJiWrong()){
            id = R.drawable.i_flag;
        }else if(entity.isBiaoJi() && entity.isBiaoJiWrong()){
            id = R.drawable.i14;
        }else if(!entity.isShow()){
            id = R.drawable.i00;
        }else if(entity.isLei() && entity.isAutoShow()){
            id = R.drawable.i12;
        }else if(entity.isLei() && !entity.isAutoShow()){
            id = R.drawable.i13;
        }else if(entity.getLeiCount() == 0){
            id = R.drawable.i09;
        }else{
            id = context.getResources().getIdentifier("i0"+entity.getLeiCount(), "drawable", context.getPackageName());
        }
        return id;
    }

    public boolean isWin(){
        return entity.isWin();
    }

    @Override
    public ItemEntity getItem(int position) {
        return entity.getEntity(position);
    }

    public void showLei(){
        entity.showLei();
        notifyDataSetChanged();
    }

    public void shouNoLei(int position){
        entity.showNoLei(position);
        notifyDataSetChanged();
    }

    public void showBiaoJi(){
        entity.showBiaoJi();
        notifyDataSetChanged();
    }
}
