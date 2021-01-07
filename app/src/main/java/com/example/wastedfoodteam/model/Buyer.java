package com.example.wastedfoodteam.model;

import java.sql.Date;

/**
 * Buyer class
 * Author Vutt
 */
public class Buyer extends Account{
//    int account_id;
    String name;
    Date date_of_birth;
    String image;
    int gender;

    public Buyer() {
    }


    public Buyer(int id, int role_id, String username, String password, String phone, String third_party_id, String email, Date create_date, int is_active, String name, Date date_of_birth, String image, int gender , String firebaseUID) {
        super(id, role_id, username, password, phone, third_party_id, email, create_date, is_active ,firebaseUID);
        this.name = name;
        this.date_of_birth = date_of_birth;
        this.image = image;
        this.gender = gender;
    }
//    public Buyer(int account_id,String name, Date date_of_birth, String image, boolean gender) {
//        this.account_id = account_id;
//        this.name = name;
//        this.date_of_birth = date_of_birth;
//        this.image = image;
//        this.gender = gender;
//    }
//
//    public int getAccount_id() {
//        return account_id;
//    }
//
//    public void setAccount_id(int account_id) {
//        this.account_id = account_id;
//    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
