package com.example.oops.EntityClass;

import java.io.Serializable;

public class LoginEntity implements Serializable {

    String userId  , password ,deviceId;

    public LoginEntity(String email, String password,String deviceId) {
        this.userId = email;
        this.password = password;
        this.deviceId = deviceId;
    }

    public String getEmail() {
        return userId;
    }

    public void setEmail(String email) {
        this.userId = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
