package com.example.e_learning.data;

public class Course {
    String courseName;
    String grade;
    String projectGrade;

    public String getCourseId() {
        return courseId;
    }

    String courseId;

    public Course() {
    }

    public Course(String courseName, String grade, String projectGrade, String attendance, String docId, String courseId) {
        this.courseName = courseName;
        this.grade = grade;
        this.projectGrade = projectGrade;
        this.attendance = attendance;
        this.docId = docId;
        this.courseId = courseId;
    }

    String attendance;
    String docId;

    public String getCourseName() {
        return courseName;
    }

    public String getGrade() {
        return grade;
    }

    public String getProjectGrade() {
        return projectGrade;
    }

    public String getAttendance() {
        return attendance;
    }

    public String getDocId() {
        return docId;
    }
}
