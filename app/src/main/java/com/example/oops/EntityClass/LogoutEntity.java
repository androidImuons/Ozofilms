package com.example.oops.EntityClass;

public class LogoutEntity {
    int id;
    String userId;

    public LogoutEntity(int id, String userId) {
        this.id = id;
        this.userId = userId;
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
}
