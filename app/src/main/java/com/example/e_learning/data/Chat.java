package com.example.e_learning.data;

public class Chat {
    private String message;
    private String userName;
    private String senderId;

    public Chat() {}

    public Chat(String message, String senderId, String userName) {
        this.message = message;
        this.senderId = senderId;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}

