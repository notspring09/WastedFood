package com.example.wastedfoodteam.buyer.buy;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.utils.CommonFunction;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private final Context context;
    private final int layout;
    private List<BuyerProduct> productList;
    final Resources resources;

    public ProductAdapter(Context context, int layout, List<BuyerProduct> productList, Resources resources) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
        this.resources = resources;
    }

    private static class ViewHolder {
        TextView tvName, tvDiscount, tvQuantity, tvOriginalPrice, tvSellPrice, tvOpenTime, tvDistance, tvRating;
        ImageView ivProduct;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Product getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

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

        BuyerProduct product = productList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvSellPrice.setText(CommonFunction.getCurrency(product.getSell_price()));
        holder.tvOriginalPrice.setText(CommonFunction.getCurrency(product.getOriginal_price()));
        holder.tvOpenTime.setText(CommonFunction.getOpenClose(product.getStart_time(), product.getEnd_time()));
        holder.tvDiscount.setText(CommonFunction.getDiscount(product.getSell_price(), product.getOriginal_price()));
        holder.tvDistance.setText(CommonFunction.getStringDistance(product.getSeller(), Variable.gps));
        CommonFunction.setQuantityTextView(holder.tvQuantity, product.getRemain_quantity(), product.getOriginal_quantity());
        CommonFunction.setImageViewSrc(context, product.getImage(), holder.ivProduct);
        CommonFunction.setDrawableForTextView(holder.tvOpenTime, R.drawable.ic_icons8_clock, context);
        CommonFunction.setDrawableForTextView(holder.tvDistance, R.drawable.location, context);
        holder.tvRating.setText(product.getSeller().getRating() + "");
        return convertView;

    }

    private void mappingViewToHolder(@NotNull ViewHolder holder, @NotNull View convertView) {
        holder.tvName = convertView.findViewById(R.id.tvTitleLPI);
        holder.ivProduct = convertView.findViewById(R.id.ivProductLPI);
        holder.tvDistance = convertView.findViewById(R.id.tvDistance);
        holder.tvDiscount = convertView.findViewById(R.id.tvDiscount);
        holder.tvQuantity = convertView.findViewById(R.id.tvQuantity);
        holder.tvOpenTime = convertView.findViewById(R.id.tvOpenTime);
        holder.tvOriginalPrice = convertView.findViewById(R.id.tvOriginalPrice);
        holder.tvSellPrice = convertView.findViewById(R.id.tvSellPrice);
        holder.tvRating = convertView.findViewById(R.id.tvRating);
    }

    public List<BuyerProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<BuyerProduct> productList) {
        this.productList = productList;
    }
}
