package com.example.wastedfoodteam.buyer.order;

import com.example.wastedfoodteam.buyer.buy.BuyerProduct;
import com.example.wastedfoodteam.model.Order;

public class BuyerOrder extends Order {
    private BuyerProduct product;


    public BuyerOrder(int buyer_id, int product_id, int quantity, Status status, double total_cost) {
        super(buyer_id, product_id, quantity, status, total_cost);
    }


    public BuyerProduct getProduct() {
        return product;
    }

    public void setProduct(BuyerProduct product) {
        this.product = product;
    }
}
