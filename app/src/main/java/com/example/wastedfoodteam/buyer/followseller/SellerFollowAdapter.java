package com.example.wastedfoodteam.buyer.followseller;

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
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.utils.CommonFunction;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SellerFollowAdapter extends BaseAdapter {
    private final Context context;
    private final int layout;
    private final List<Seller> sellerList;
    final Resources resources;

    public SellerFollowAdapter(Context context, int layout, List<Seller> sellerList, Resources resources) {
        this.context = context;
        this.layout = layout;
        this.sellerList = sellerList;
        this.resources = resources;
    }

    @Override
    public int getCount() {
        return sellerList.size();
    }

    @Override
    public Object getItem(int position) {
        return sellerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return sellerList.get(position).getId();
    }

    private static class ViewHolder {
        ImageView ivSeller;
        TextView tvNameSeller, tvDirection, tvAddress,tvRating;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            mappingViewWithHolderItems(holder, convertView);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Seller seller = sellerList.get(position);

        holder.tvNameSeller.setText(seller.getName());
        holder.tvAddress.setText(seller.getAddress());
        holder.tvDirection.setText(CommonFunction.getStringDistance(seller, Variable.gps) );
        holder.tvRating.setText(seller.getRating() + "");
        CommonFunction.setImageViewSrc(context, seller.getImage(), holder.ivSeller);
        return convertView;
    }

    private void mappingViewWithHolderItems(@NotNull ViewHolder holder, @NotNull View convertView) {
        holder.tvNameSeller = convertView.findViewById(R.id.tvNameSellerFSFI);
        holder.tvAddress = convertView.findViewById(R.id.tvAddress);
        holder.ivSeller = convertView.findViewById(R.id.ivSellerLSFI);
        holder.tvDirection = convertView.findViewById(R.id.tvDistance);
        holder.tvRating = convertView.findViewById(R.id.tvRating);
        convertView.setTag(holder);
    }
}
