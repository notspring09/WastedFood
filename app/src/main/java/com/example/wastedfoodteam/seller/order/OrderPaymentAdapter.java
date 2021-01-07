package com.example.wastedfoodteam.seller.order;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Order;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.seller.notification.NotificationUtil;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.notification.SendNotif;
import com.example.wastedfoodteam.utils.service.UpdateStatusForOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderPaymentAdapter extends BaseAdapter {
    final Context myContext;
    final int myLayout;
    final List<SellerOrder> arrayOrder;
    final SendNotif sendNotif = new SendNotif();
    NotificationUtil util;
    Product product;
    SellerOrder order;
    final Resources resources;
    final FragmentActivity myFragmentActivity;

    private static class ViewHolder {
        ImageView ivBuyer;
        Button btnConfirm,btnReject;
        TextView tvQuantity,tvTotalCost;
    }

    public OrderPaymentAdapter(Context context, int layout, List<SellerOrder> orderList , Resources resources , FragmentActivity fragmentActivity ){
        myContext = context;
        myLayout = layout;
        arrayOrder = orderList;
        this.resources = resources;
        myFragmentActivity = fragmentActivity;
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
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(myLayout, null);
            holder.tvQuantity = convertView.findViewById(R.id.tv_list_seller_payment_quantity);
            holder.tvTotalCost = convertView.findViewById(R.id.tv_list_seller_payment_cost);
            holder.btnConfirm = convertView.findViewById(R.id.btn_list_seller_payment_confirm);
            holder.btnReject = convertView.findViewById(R.id.btn_list_seller_payment_reject);
            holder.ivBuyer = convertView.findViewById(R.id.iv_list_seller_payment_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        util = new NotificationUtil();
        order = arrayOrder.get(position);
        product = Variable.PRODUCT;
        CommonFunction.setImageViewSrc(myContext,order.getBuyer_avatar(),holder.ivBuyer);
        holder.tvTotalCost.setText(myContext.getString(R.string.total_money, CommonFunction.getCurrency(order.getTotal_cost())));
        holder.tvQuantity.setText(myContext.getString(R.string.total_quantity, order.getQuantity()));
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status = done
                //reload fragment
                UpdateStatusForOrder.updateOrderStatus(Variable.IP_ADDRESS + "seller/updateStatusForOrderSeller.php",Order.Status.SUCCESS, order.getId(),myContext,myFragmentActivity);
                String message = Variable.SELLER.getName() + " đã xác nhận thanh toán của bạn\r Cảm ơn bạn vì đã sử dụng dịch vụ của chúng tôi";
                util.addNotification(myContext,Variable.SELLER.getId() , order.getBuyer_id(), message , order.getId());
                sendNotif.notificationHandle(order.getFirebase_UID(), "Wasted food app" , message);

            }
        });
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status = rejected
                //set btnReject text to Đã từ chối set clickable = false
                UpdateStatusForOrder.updateOrderStatus(Variable.IP_ADDRESS + "seller/updateStatusForOrderSeller.php",Order.Status.CANCEL, order.getId(),myContext,myFragmentActivity);
                String message = Variable.SELLER.getName() + " đã hủy đơn hàng của bạn nếu có thắc mắc vui lòng thông báo với chúng tôi qua địa chỉ email hoặc chức năng báo cáo";
                util.addNotification(myContext,Variable.SELLER.getId() , order.getBuyer_id(), message , order.getId());
                updateRemainQuantity(product.getId());
                sendNotif.notificationHandle(order.getFirebase_UID(), "Wasted food app" , message);
            }
        });
        return convertView;
    }
    private void updateRemainQuantity( final int id){
        String url = Variable.IP_ADDRESS + "seller/updateRemainQuantityProduct.php";
        RequestQueue requestQueue = Volley.newRequestQueue(myContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Succesfully update")){
                            //Toast.makeText(myContext,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText( myContext,"Lỗi cập nhật",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(myContext,"Xảy ra lỗi, vui lòng thử lại",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                int remain_quantity =  product.getRemain_quantity() + order.getQuantity();
                product.setRemain_quantity(remain_quantity);
                params.put("remain_quantity", String.valueOf(remain_quantity)  );
                params.put("id" , String.valueOf(id));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
