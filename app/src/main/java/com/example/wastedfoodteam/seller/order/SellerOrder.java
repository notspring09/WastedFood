package com.example.wastedfoodteam.seller.order;

import com.example.wastedfoodteam.model.Order;

public class SellerOrder extends Order {

    String buyer_name;
    String product_name;
    String product_image;
    String buyer_avatar;

    public SellerOrder(String buyer_name, String product_name, String product_image, String buyer_avatar) {
        this.buyer_name = buyer_name;
        this.product_name = product_name;
        this.product_image = product_image;
        this.buyer_avatar = buyer_avatar;
    }

    public SellerOrder(int buyer_id, int product_id, int quantity, Status status, double total_cost, String buyer_name, String product_name, String product_image, String buyer_avatar) {
        super(buyer_id, product_id, quantity, status, total_cost);
        this.buyer_name = buyer_name;
        this.product_name = product_name;
        this.product_image = product_image;
        this.buyer_avatar = buyer_avatar;
    }

    public SellerOrder(int id, int buyer_id, int product_id, int quantity, Status status, double total_cost, int buyer_rating, String buyer_comment, String buyer_name, String product_name, String product_image, String buyer_avatar , String firebase_UID) {
        super(id, buyer_id, product_id, quantity, status, total_cost, buyer_rating, buyer_comment, firebase_UID);
        this.buyer_name = buyer_name;
        this.product_name = product_name;
        this.product_image = product_image;
        this.buyer_avatar = buyer_avatar;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getBuyer_avatar() {
        return buyer_avatar;
    }

    public void setBuyer_avatar(String buyer_avatar) {
        this.buyer_avatar = buyer_avatar;
    }
}
