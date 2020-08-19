package com.example.oops.EntityClass;

import java.io.Serializable;

public class ChangePasswordEntitiy implements Serializable {
    String userid,oldPassword,newPassword;
    int id;

    public ChangePasswordEntitiy(int id, String userid, String oldPassword, String newPassword) {
        this.id = id;
        this.userid = userid;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
