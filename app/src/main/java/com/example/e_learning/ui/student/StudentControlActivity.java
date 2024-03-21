package com.example.e_learning.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.databinding.ActivityStudentControlBinding;
import com.example.e_learning.util.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class StudentControlActivity extends AppCompatActivity {
    private ActivityStudentControlBinding binding;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentControlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        UserPreferences userPreferences = new UserPreferences(this);
        String name = userPreferences.getStudentName();
        Intent intent = getIntent();
        courseId = intent.getStringExtra("courseId");
        String courseName = intent.getStringExtra("courseName");

        binding.studentName.setText(name);
        binding.courseName.setText(courseName);

        binding.grade.setOnClickListener(v -> {
            Intent intent1 = new Intent(StudentControlActivity.this, StudentGradesActivity.class);
            intent1.putExtra("courseId", courseId);
            startActivity(intent1);
        });

        binding.solveQuiz.setOnClickListener(v -> {
            Intent intent12 = new Intent(StudentControlActivity.this, CourseQuizFroStudentActivity.class);
            intent12.putExtra("courseId", courseId);
            startActivity(intent12);
        });

        binding.btnActive.setOnClickListener(v -> {
            String code = Objects.requireNonNull(binding.enterCourseCode.getEditText()).getText().toString().trim();
            if (code.isEmpty()) {
                binding.enterCourseCode.setError("Required");
            } else {
                checkCode(courseId, code);
            }
        });
        binding.material.setOnClickListener(v -> {
            Intent intent13 = new Intent(StudentControlActivity.this, MaterialForStudentActivity.class);
            intent13.putExtra("courseId", courseId);
            startActivity(intent13);
        });
        binding.chats.setOnClickListener(v -> {
            Intent intent14 = new Intent(StudentControlActivity.this, StudentChatCourse.class);
            intent14.putExtra("courseId", courseId);
            startActivity(intent14);
        });
    }

    private void checkCode(String courseId, String code) {
        ref.child("attendance").child(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(code)) {
                    if (snapshot.child(code).hasChild(Objects.requireNonNull(auth.getCurrentUser()).getUid())) {
                        Toast.makeText(StudentControlActivity.this, "You have already attended", Toast.LENGTH_SHORT).show();
                    } else {
                        registerAttendance(courseId, code);
                    }
                } else {
                    Toast.makeText(StudentControlActivity.this, "Wrong code😪, Please enter a valid code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }

    private void registerAttendance(String courseId, String code) {
        ref.child("attendance").child(courseId).child(code)
                .child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .setValue(auth.getCurrentUser().getUid())
                .addOnSuccessListener(unused -> {
                    saveAttendance(courseId);
                    Toast.makeText(StudentControlActivity.this, "Done✅", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e ->
                        Toast.makeText(
                                StudentControlActivity.this,
                                e.getLocalizedMessage(),
                                Toast.LENGTH_SHORT
                        ).show());
    }

    private void saveAttendance(String courseId) {
        String studentId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DatabaseReference attendanceRef = ref.child("courses")
                .child(courseId)
                .child("course students")
                .child(studentId)
                .child("attendance");

        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String attendance = dataSnapshot.getValue(String.class);
                if (attendance != null) {
                    int attendanceValue = Integer.parseInt(attendance);
                    attendanceValue++;
                    dataSnapshot.getRef().setValue(String.valueOf(attendanceValue));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}