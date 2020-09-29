package com.example.oops.EntityClass;

public class RegistrationEntity {
    String name ,email, password,phone,subscriptionType , pin,deviceId;

    public RegistrationEntity(String name, String email, String password, String phone, String subscriptionType, String pin,String deviceId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.subscriptionType = subscriptionType;
        this.pin = pin;
        this.deviceId = deviceId;
    }
}
