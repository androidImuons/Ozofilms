package com.example.oops.EntityClass;

import java.io.Serializable;

public class ChangePinEntity  implements Serializable {
    String  userId, oldPin,newPin;
    int id;


    public ChangePinEntity(String userId, String oldPin, String newPin, int id) {
        this.userId = userId;
        this.oldPin = oldPin;
        this.newPin = newPin;
        this.id = id;
    }
}