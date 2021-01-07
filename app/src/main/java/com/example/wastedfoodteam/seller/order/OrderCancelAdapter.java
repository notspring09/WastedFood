package com.example.wastedfoodteam.seller.order;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Buyer;
import com.example.wastedfoodteam.utils.CameraStorageFunction;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.ReportDialog;

import java.util.List;

public class OrderCancelAdapter extends BaseAdapter {
    final Context myContext;
    final int myLayout;
    final List<SellerOrder> arrayOrder;
    SellerOrder order;
    final Activity myFragmentActivity;
    final Resources resources;
    CameraStorageFunction cameraStorageFunction;

    private static class ViewHolder {
        ImageView ivBuyer;
        Button btnCancel;
        TextView tvQuantity, tvTotalCost;
    }

    public OrderCancelAdapter(Context context, int layout, List<SellerOrder> orderList, Resources resources, Activity fragmentActivity, CameraStorageFunction cameraStorageFunction) {
        myContext = context;
        myLayout = layout;
        arrayOrder = orderList;
        this.resources = resources;
        myFragmentActivity = fragmentActivity;
        this.cameraStorageFunction = cameraStorageFunction;
    }


    @Override
    public int getCount() {
        return arrayOrder.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayOrder.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrayOrder.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(myLayout, null);
            holder.tvQuantity = convertView.findViewById(R.id.tv_list_seller_cancel_quantity);
            holder.tvTotalCost = convertView.findViewById(R.id.tv_list_seller_cancel_cost);
            holder.btnCancel = convertView.findViewById(R.id.btn_list_seller_cancel_cancel);
            holder.ivBuyer = convertView.findViewById(R.id.iv_list_seller_cancel_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        order = arrayOrder.get(position);
        CommonFunction.setImageViewSrc(myContext, order.getBuyer_avatar(), holder.ivBuyer);
        holder.tvTotalCost.setText(myContext.getString(R.string.total_money, CommonFunction.getCurrency(order.getTotal_cost())));
        holder.tvQuantity.setText(myContext.getString(R.string.total_quantity, order.getQuantity()));
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Buyer b = new Buyer();
                b.setId(order.getBuyer_id());
                b.setName(order.getBuyer_name());
                ReportDialog reportDialog = new ReportDialog(myFragmentActivity,
                        (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE), b,
                        cameraStorageFunction,
                        Variable.SELLER.getId() + "");
                reportDialog.displayReportDialog();
            }
        });
        return convertView;
    }
}
