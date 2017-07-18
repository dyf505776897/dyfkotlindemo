package com.dyf.dyfkotlindemo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyf.dyfkotlindemo.R;
import com.dyf.dyfkotlindemo.model.Goods;
import com.dyf.dyfkotlindemo.model.Info;

import org.w3c.dom.Text;

public class SampleAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Info> list;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    LinearLayout mLview;
    private Context mContext;

    public List<Info> getList() {
        return list;
    }

    public SampleAdapter(Context context) {
        list = new ArrayList<Info>();
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER;
//        } else {
//            return TYPE_ITEM;
//        }
//    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).order_sn_tv.setText("订单编号:" + String.valueOf(list.get(position).getOrder_sn()));
            ((ItemViewHolder) holder).order_time_tv.setText("下单时间:" + String.valueOf(list.get(position).getTime()));

            //
            ((ItemViewHolder) holder).item_content.removeAllViews();
            List<Goods> goods = list.get(position).getGoodslist();
            for (int i = 0; i < goods.size(); i++){
                View view = LayoutInflater.from(mContext).inflate(R.layout.r_item_r, null);
                TextView name = (TextView) view.findViewById(R.id.name);
                name.setText(goods.get(i).getGname());
                ((ItemViewHolder) holder).item_content.addView(view);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_item, null);
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            return new ItemViewHolder(view);
        }
//        else if (viewType == TYPE_FOOTER) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footerview, null);
//            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//            return new FooterViewHolder(view);
//        }

        return null;
    }

    class FooterViewHolder extends ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    class ItemViewHolder extends ViewHolder {
        TextView order_sn_tv;
        TextView order_time_tv;
        LinearLayout item_content;

        public ItemViewHolder(View view) {
            super(view);
            order_sn_tv = (TextView) view.findViewById(R.id.order_sn_tv);
            order_time_tv = (TextView) view.findViewById(R.id.order_time_tv);
            item_content = (LinearLayout) view.findViewById(R.id.item_content);
        }

    }

}
