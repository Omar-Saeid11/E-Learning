package com.example.e_learning.data;

import java.util.HashMap;

public class InfoStudent {
    private String attendance;
    private String attendanceTotal;
    private String courseId;
    private String courseName;
    private HashMap<String, Integer> quizGrade;
    private HashMap<String, Integer> stGrade;
    private String studentId;
    private String studentName;

    public InfoStudent() {
        // Default constructor required for Firebase
    }

    public InfoStudent(String attendance, String attendanceTotal, String courseId, String courseName, HashMap<String, Integer> quizGrade, HashMap<String, Integer> stGrade, String studentId, String studentName) {
        this.attendance = attendance;
        this.attendanceTotal = attendanceTotal;
        this.courseId = courseId;
        this.courseName = courseName;
        this.quizGrade = quizGrade;
        this.stGrade = stGrade;
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getAttendanceTotal() {
        return attendanceTotal;
    }

    public void setAttendanceTotal(String attendanceTotal) {
        this.attendanceTotal = attendanceTotal;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public HashMap<String, Integer> getQuizGrade() {
        return quizGrade;
    }

    public void setQuizGrade(HashMap<String, Integer> quizGrade) {
        this.quizGrade = quizGrade;
    }

    public HashMap<String, Integer> getStGrade() {
        return stGrade;
    }

    public void setStGrade(HashMap<String, Integer> stGrade) {
        this.stGrade = stGrade;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
