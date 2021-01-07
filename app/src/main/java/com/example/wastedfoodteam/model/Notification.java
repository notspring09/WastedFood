package com.example.wastedfoodteam.model;

import java.util.Date;

public class Notification {
    private int id;
    private int sender_id;
    private int receiver_id;
    private String content;
    private Date modified_date;
    private int order_id;
    private String seller_image;
    private String buyer_image;
    private String seller_name;
    private String buyer_name;
    private String product_name;
    private String product_image;
    private Boolean seen;


    public Notification() {

    }

    public Notification(int id, int sender_id, int receiver_id, String content, Date modified_date, int order_id, String seller_image, String buyer_image, String seller_name, String buyer_name, String product_name, String product_image , Boolean seen) {
        this.id = id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.content = content;
        this.modified_date = modified_date;
        this.order_id = order_id;
        this.seller_image = seller_image;
        this.buyer_image = buyer_image;
        this.seller_name = seller_name;
        this.buyer_name = buyer_name;
        this.product_name = product_name;
        this.product_image = product_image;
        this.seen = seen;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getModified_date() {
        return modified_date;
    }

    public void setModified_date(Date modified_date) {
        this.modified_date = modified_date;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getSeller_image() {
        return seller_image;
    }

    public void setSeller_image(String seller_image) {
        this.seller_image = seller_image;
    }

    public String getBuyer_image() {
        return buyer_image;
    }

    public void setBuyer_image(String buyer_image) {
        this.buyer_image = buyer_image;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
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
}
