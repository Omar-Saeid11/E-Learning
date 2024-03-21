package com.example.e_learning.ui.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.R;
import com.example.e_learning.data.Course;
import com.example.e_learning.databinding.ActivityCreateCourseBinding;
import com.example.e_learning.util.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CreateCourse extends AppCompatActivity {
    ActivityCreateCourseBinding binding;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.confirm.setOnClickListener(v -> {
            String courseName = Objects.requireNonNull(binding.courseName.getEditText()).getText().toString().trim();
            String grade = Objects.requireNonNull(binding.courseGrade.getEditText()).getText().toString().trim();
            String projectGrade = Objects.requireNonNull(binding.projectGrade.getEditText()).getText().toString().trim();
            String attendance = Objects.requireNonNull(binding.attendance.getEditText()).getText().toString().trim();
            String id = ref.push().getKey();
            assert id != null;
            UserPreferences userPreferences = new UserPreferences(CreateCourse.this);
            userPreferences.setCourseId(id);
            validate(courseName, grade, projectGrade, attendance, id);
            binding.courseName.getEditText().setText("");
            binding.courseGrade.getEditText().setText("");
            binding.projectGrade.getEditText().setText("");
            binding.attendance.getEditText().setText("");
        });
        binding.back.setOnClickListener(v -> {
            startActivity(new Intent(CreateCourse.this, DoctorActivity.class));
            finish();
        });
    }

    private void validate(String courseName, String grade, String projectGrade, String attendance, String courseId) {
        if (courseName.isEmpty()) {
            binding.courseName.setError(getText(R.string.required));
        } else if (grade.isEmpty()) {
            binding.courseGrade.setError(getText(R.string.required));
        } else if (projectGrade.isEmpty()) {
            binding.projectGrade.setError(getText(R.string.required));
        } else if (attendance.isEmpty()) {
            binding.attendance.setError(getText(R.string.required));
        } else {
            getData(courseName, grade, projectGrade, attendance, courseId);
        }
    }

    private void getData(String courseName, String grade, String projectGrade, String attendance, String courseId) {
        ref.child("courses")
                .child(courseId)
                .setValue(
                        new Course(
                                courseName,
                                grade,
                                projectGrade,
                                attendance,
                                Objects.requireNonNull(auth.getCurrentUser()).getUid(), courseId))
                .addOnFailureListener(e -> Toast.makeText(CreateCourse.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
    }
}