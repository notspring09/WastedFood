package com.example.wastedfoodteam.buyer.followseller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.buy.BuyerProduct;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.utils.CommonFunction;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductAdapterOfSeller extends BaseAdapter {
    private final Context context;
    private final int layout;
    private final List<BuyerProduct> productList;
    final Resources resources;

    public ProductAdapterOfSeller(Context context, int layout, List<BuyerProduct> productList, Resources resources) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
        this.resources = resources;
    }

    private static class ViewHolder {
        TextView tvName,tvDiscount,tvQuantity,tvOriginalPrice,tvSellPrice;
        ImageView ivProduct;
    }
    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductAdapterOfSeller.ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            mappingViewWithHolderItem(holder, convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);

        holder.tvName.setText(product.getName() + "");
        holder.tvSellPrice.setText(CommonFunction.getCurrency(product.getSell_price()));
        holder.tvOriginalPrice.setText(CommonFunction.getCurrency(product.getOriginal_price()));
        holder.tvDiscount.setText("Sale " + CommonFunction.getDiscount(product.getSell_price(),product.getOriginal_price()));
        CommonFunction.setQuantityTextView(holder.tvQuantity,product.getRemain_quantity(),product.getOriginal_quantity());
        //get image from url
        CommonFunction.setImageViewSrc(context,product.getImage(),holder.ivProduct);

        return convertView;
    }

    private void mappingViewWithHolderItem(@NotNull ViewHolder holder, @NotNull View convertView) {
        holder.tvName = convertView.findViewById(R.id.tvNameLSPI);
        holder.ivProduct = convertView.findViewById(R.id.ivProductLSPI);
        holder.tvDiscount = convertView.findViewById(R.id.tvDiscount);
        holder.tvQuantity = convertView.findViewById(R.id.tvQuantity);
        holder.tvOriginalPrice = convertView.findViewById(R.id.tvOriginalPrice);
        holder.tvSellPrice = convertView.findViewById(R.id.tvSellPrice);
        convertView.setTag(holder);
    }


}
