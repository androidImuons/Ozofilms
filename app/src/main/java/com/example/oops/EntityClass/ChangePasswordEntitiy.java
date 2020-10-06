package com.example.oops.EntityClass;

import java.io.Serializable;

public class ChangePasswordEntitiy implements Serializable {
    String userId,oldPassword,newPassword;
    String id;

    public ChangePasswordEntitiy(String userId, String oldPassword, String newPassword, String id) {
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.id = id;
    }
}
