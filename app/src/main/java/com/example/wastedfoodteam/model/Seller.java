package com.example.wastedfoodteam.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Seller class
 * author Vutt
 */
public class Seller extends Account implements Serializable {

    String name;
    String image;
    String address;
    double latitude;
    double longitude;
    double rating;
    String description;

    //add for get date easier
    double distance;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Seller() {
    }

    public Seller(int id, int role_id, String username, String password, String phone, String third_party_id, String email, Date create_date, int is_active, String name, String image, String address, double latitude, double longitude, String description , String firebase_UID) {
        super(id, role_id, username, password, phone, third_party_id, email, create_date, is_active ,firebase_UID);
        this.name = name;
        this.image = image;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    /**
     * constructor for distance
     * @param distance
     */
    public Seller(int id, int role_id, String username, String password, String phone, String third_party_id, String email, Date create_date, int is_active, String name, String image, String address, double latitude, double longitude, String description, double distance , String firebase_UID) {
        super(id, role_id, username, password, phone, third_party_id, email, create_date, is_active,firebase_UID);
        this.name = name;
        this.image = image;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.distance = distance;
    }

    public Seller(int id, int role_id, String username, String password, String phone, String third_party_id, String email, Date create_date, int is_active, String firebase_UID, String name, String image, String address, double latitude, double longitude, double rating, String description, double distance) {
        super(id, role_id, username, password, phone, third_party_id, email, create_date, is_active, firebase_UID);
        this.name = name;
        this.image = image;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.description = description;
        this.distance = distance;
    }
}
