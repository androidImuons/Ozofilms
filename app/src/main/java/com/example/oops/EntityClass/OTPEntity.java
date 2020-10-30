package com.example.oops.EntityClass;

public class OTPEntity {
    String  user_id, otp ,deviceId;
    int id;

    public OTPEntity(int id, String user_id, String otp, String deviceId) {
        this.id = id;
        this.user_id = user_id;
        this.otp = otp;
        this.deviceId = deviceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
