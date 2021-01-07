package com.example.wastedfoodteam.buyer.buy;

import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.model.Seller;

import java.util.Date;

public class BuyerProduct extends Product {
    private Seller seller;

    public BuyerProduct(int id, int seller_id, String name, String image, Date start_time, Date end_time, double original_price, double sell_price, int original_quantity, int remain_quantity, String description, Date sell_date, ProductStatus status, boolean shippable, Seller seller) {
        super(id, seller_id, name, image, start_time, end_time, original_price, sell_price, original_quantity, remain_quantity, description, sell_date, status, shippable);
        this.seller = seller;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
