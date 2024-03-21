package com.example.e_learning.ui.doctor;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.databinding.ActivityDoctorControlPageBinding;

public class DoctorControlPage extends AppCompatActivity {
    ActivityDoctorControlPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorControlPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String courseId = getIntent().getStringExtra("courseId");
        binding.codeCourse.setText(courseId);
        binding.codeCourse.setOnClickListener(v -> {
            String textToCopy = binding.codeCourse.getText().toString();
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Copied Text", textToCopy);
            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(getApplicationContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
        });
        binding.btnAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorControlPage.this, AttendanceActivity.class);
            intent.putExtra("courseId", courseId);
            startActivity(intent);
        });
        binding.btnCreateQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorControlPage.this, QuizActivity.class);
            intent.putExtra("courseId", courseId);
            startActivity(intent);
        });
        binding.btnUploadMaterial.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorControlPage.this, UploadMaterialActivity.class);
            intent.putExtra("courseId", courseId);
            startActivity(intent);
        });
        binding.btnShowGrades.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorControlPage.this, GradesActivity.class);
            intent.putExtra("courseId", courseId);
            startActivity(intent);
        });
    }
}