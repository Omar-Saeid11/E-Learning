package com.example.e_learning.data;

public class User {
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    String name;
    String email;

    public User(String name, String email, String userId,String userType) {
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.userType=userType;
    }

    public User() {
    }

    String userId;

    public String getUserType() {
        return userType;
    }

    String userType;
}
