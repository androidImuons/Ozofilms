package com.example.oops.EntityClass;

import java.io.Serializable;

public class SupportHelpEntity implements Serializable {
    String name,mobile,email,message;


    public SupportHelpEntity(String name, String mobile, String email, String message) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
