package com.example.oops.EntityClass;

import java.io.Serializable;

public class ProfileEntity implements Serializable {
    int id;

    String userId , email,phone;

    public ProfileEntity(int id, String userId, String email, String phone) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
