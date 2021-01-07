package com.example.wastedfoodteam.model;

import java.io.Serializable;

/**
 * Order class
 * author Vutt
 */
public class Order implements Serializable {
    int id;
    int buyer_id;
    int product_id;
    int quantity;
    Status status;
    double total_cost;
    int buyer_rating;
    String buyer_comment;
    String firebase_UID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public int getBuyer_rating() {
        return buyer_rating;
    }

    public void setBuyer_rating(int buyer_rating) {
        this.buyer_rating = buyer_rating;
    }

    public String getBuyer_comment() {
        return buyer_comment;
    }

    public void setBuyer_comment(String buyer_comment) {
        this.buyer_comment = buyer_comment;
    }

    public String getFirebase_UID() {
        return firebase_UID;
    }

    public void setFirebase_UID(String firebase_UID) {
        this.firebase_UID = firebase_UID;
    }

    public boolean isNotRatingYet() {
        return this.status == Order.Status.SUCCESS && this.buyer_comment == null;
    }

    public Order() {
    }


    public Order(int buyer_id, int product_id, int quantity, Status status, double total_cost) {
        this.buyer_id = buyer_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.status = status;
        this.total_cost = total_cost;
    }

    public Order(int id, int buyer_id, int product_id, int quantity, Status status, double total_cost, int buyer_rating, String buyer_comment) {
        this.id = id;
        this.buyer_id = buyer_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.status = status;
        this.total_cost = total_cost;
        this.buyer_rating = buyer_rating;
        this.buyer_comment = buyer_comment;
    }

    public Order(int id, int buyer_id, int product_id, int quantity, Status status, double total_cost, int buyer_rating, String buyer_comment, String firebase_UID) {
        this.id = id;
        this.buyer_id = buyer_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.status = status;
        this.total_cost = total_cost;
        this.buyer_rating = buyer_rating;
        this.buyer_comment = buyer_comment;
        this.firebase_UID = firebase_UID;
    }

    public enum Status {
        BUYING,
        SUCCESS,
        CANCEL,

    }
}
