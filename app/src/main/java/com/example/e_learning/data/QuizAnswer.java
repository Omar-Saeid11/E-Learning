package com.example.e_learning.data;

public class QuizAnswer {
    private String userName;
    private String userId;
    private int grade;

    public QuizAnswer(String userName, String userId, int grade) {
        this.userName = userName;
        this.userId = userId;
        this.grade = grade;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
