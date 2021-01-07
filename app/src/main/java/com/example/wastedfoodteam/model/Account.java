package com.example.wastedfoodteam.model;

import java.io.Serializable;
import java.sql.Date;

public class Account implements Serializable {
    private int id;
    private int role_id;
    private String username;
    private String password;
    private String phone;
    private String third_party_id;
    private String email;
    private Date create_date;
    private  int is_active;
    private String firebase_UID;


    public Account() {
    }

    public Account(int id, int role_id, String username, String password, String phone, String third_party_id, String email, Date create_date, int is_active, String firebase_UID) {
        this.id = id;
        this.role_id = role_id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.third_party_id = third_party_id;
        this.email = email;
        this.create_date = create_date;
        this.is_active = is_active;
        this.firebase_UID = firebase_UID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getThird_party_id() {
        return third_party_id;
    }

    public void setThird_party_id(String third_party_id) {
        this.third_party_id = third_party_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getFirebase_UID() {
        return firebase_UID;
    }

    public void setFirebase_UID(String firebase_UID) {
        this.firebase_UID = firebase_UID;
    }
}
