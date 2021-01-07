package com.example.wastedfoodteam.buyer.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.utils.CommonFunction;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private final Context context;
    private final int layout;
    private final List<BuyerOrder> orderList;
    final Resources resources;

    public OrderAdapter(Context context, int layout, List<BuyerOrder> orderList, Resources resources) {
        this.context = context;
        this.layout = layout;
        this.orderList = orderList;
        this.resources = resources;
    }

    private static class ViewHolder {
        TextView tvName, tvOriginalPrice, tvSellPrice, tvOpenTime, tvDistance, tvStatus;
        ImageView ivProduct;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return orderList.get(position).getId();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            mappingViewToHolder(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BuyerOrder order = orderList.get(position);
        holder.tvName.setText(order.getProduct().getName());
        holder.tvSellPrice.setText(CommonFunction.getCurrency(order.getTotal_cost()));
        holder.tvOpenTime.setText(CommonFunction.getOpenClose(order.getProduct().getStart_time(), order.getProduct().getEnd_time()));
        CommonFunction.setDrawableForTextView(holder.tvOpenTime, R.drawable.ic_icons8_clock,context);
        holder.tvDistance.setText(CommonFunction.getStringDistance(order.getProduct().getSeller(), Variable.gps));
        CommonFunction.setDrawableForTextView(holder.tvDistance, R.drawable.location,context);
        setStatusTextView(holder.tvStatus, order);
        //get image from url
        CommonFunction.setImageViewSrc(context, order.getProduct().getImage(), holder.ivProduct);

        return convertView;

    }

    private void setStatusTextView(TextView tvStatus, BuyerOrder order) {
        switch (order.getStatus()) {
            case BUYING:
                tvStatus.setText(context.getString(R.string.wait_for_payment));
                tvStatus.setBackgroundColor(Color.parseColor("#A4FF84"));
                break;
            case SUCCESS:
                tvStatus.setText(context.getString(R.string.payed));
                tvStatus.setBackgroundColor(Color.parseColor("#C4C4C4"));
                break;
            case CANCEL:
                tvStatus.setText(context.getString(R.string.canceled));
                tvStatus.setBackgroundColor(Color.parseColor("#FF0000"));
                break;
        }
    }

    private void setDrawableForTextView(TextView tv, int drawableId) {
        int drawableSize = 50;
        Drawable drawable = new ScaleDrawable(context.getDrawable(drawableId), 0, drawableSize, drawableSize).getDrawable();
        drawable.setBounds(0, 0, drawableSize, drawableSize);
        tv.setCompoundDrawables(drawable, null, null, null);
    }

    private void mappingViewToHolder(ViewHolder holder, View convertView) {
        holder.tvName = convertView.findViewById(R.id.tvTitleLPI);
        holder.ivProduct = convertView.findViewById(R.id.ivProductLPI);
        holder.tvDistance = convertView.findViewById(R.id.tvDistance);
        holder.tvOpenTime = convertView.findViewById(R.id.tvOpenTime);
        holder.tvOriginalPrice = convertView.findViewById(R.id.tvOriginalPrice);
        holder.tvSellPrice = convertView.findViewById(R.id.tvSellPrice);
        holder.tvStatus = convertView.findViewById(R.id.tvStatus);
    }
}
