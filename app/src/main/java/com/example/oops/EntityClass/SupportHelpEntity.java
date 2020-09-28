package com.example.oops.EntityClass;

import java.io.Serializable;

public class SupportHelpEntity implements Serializable {
    int id;
    String userId,name,mobile,email,message;

    public SupportHelpEntity(int id, String userId, String name, String mobile, String email, String message) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.message = message;
    }
}
