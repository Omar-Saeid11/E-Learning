package com.example.e_learning.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String PREF_NAME = "UserPreferences";
    private static final String KEY_USER_TYPE = "userType";
    private static final String KEY_COURSE_ID = "courseId";
    private static final String KEY_STUDENT_NAME = "studentName";
//    private static final String KEY_ATTENDANCE = "attendance";
//    private static final String KEY_TOTAL_ATTENDANCE = "totalAttendance";



    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUserType(String userType) {
        editor.putString(KEY_USER_TYPE, userType);
        editor.apply();
    }

    public String getUserType() {
        return sharedPreferences.getString(KEY_USER_TYPE, "");
    }

    public void setCourseId(String courseId) {
        editor.putString(KEY_COURSE_ID, courseId);
        editor.apply();
    }

    public String getCourseId() {
        return sharedPreferences.getString(KEY_COURSE_ID, "");
    }

    public void setStudentName(String studentName) {
        editor.putString(KEY_STUDENT_NAME, studentName);
        editor.apply();
    }

    public String getStudentName() {
        return sharedPreferences.getString(KEY_STUDENT_NAME, "");
    }

}

