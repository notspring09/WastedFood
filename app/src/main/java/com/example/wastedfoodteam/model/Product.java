package com.example.wastedfoodteam.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Product class
 * author Vutt
 */
public class Product implements Serializable {
    int id;
    int seller_id;
    String name;
    String image;
    Date start_time;
    Date end_time;
    double original_price;
    double sell_price;
    int original_quantity;
    int remain_quantity;
    String description;
    Date sell_date;
    ProductStatus status;
    boolean shippable;

    public Product(int id, int seller_id, String name, String image, Date start_time, Date end_time, double original_price, double sell_price, int original_quantity, int remain_quantity, String description, Date sell_date, ProductStatus status, boolean shippable) {
        this.id = id;
        this.seller_id = seller_id;
        this.name = name;
        this.image = image;
        this.start_time = start_time;
        this.end_time = end_time;
        this.original_price = original_price;
        this.sell_price = sell_price;
        this.original_quantity = original_quantity;
        this.remain_quantity = remain_quantity;
        this.description = description;
        this.sell_date = sell_date;
        this.status = status;
        this.shippable = shippable;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public double getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(double original_price) {
        this.original_price = original_price;
    }

    public double getSell_price() {
        return sell_price;
    }

    public void setSell_price(double sell_price) {
        this.sell_price = sell_price;
    }

    public int getOriginal_quantity() {
        return original_quantity;
    }

    public void setOriginal_quantity(int original_quantity) {
        this.original_quantity = original_quantity;
    }

    public int getRemain_quantity() {
        return remain_quantity;
    }

    public void setRemain_quantity(int remain_quantity) {
        this.remain_quantity = remain_quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSell_date() {
        return sell_date;
    }

    public void setSell_date(Date sell_date) {
        this.sell_date = sell_date;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public boolean isShippable() {
        return shippable;
    }

    public void setShippable(boolean shippable) {
        this.shippable = shippable;
    }


    public Product() {
    }

    public enum ProductStatus {
        SELLING,
        CANCEL
    }
}
