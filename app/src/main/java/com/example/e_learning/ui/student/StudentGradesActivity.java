package com.example.e_learning.ui.student;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.databinding.ActivityStudentGradesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class StudentGradesActivity extends AppCompatActivity {
    ActivityStudentGradesBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentGradesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String courseId = intent.getStringExtra("courseId");
        getStGrade(courseId);
        getQuizGrade(courseId);
        getStAttendance(courseId);
        getTotalAttendance(courseId);
    }

    private void getStAttendance(String courseId) {
        ref.child("courses")
                .child(courseId)
                .child("course students")
                .child(userId)
                .child("attendance")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String stAttendance = dataSnapshot.getValue(String.class);
                        if (stAttendance != null) {
                            binding.stAttendance.setText(stAttendance);
                            ref.child("course students")
                                    .child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                                    .child(courseId)
                                    .child("attendance")
                                    .setValue(stAttendance);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error here
                    }
                });
    }

    private void getTotalAttendance(String courseId) {
        ref.child("courses")
                .child(courseId)
                .child("course students")
                .child(userId)
                .child("attendanceTotal")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String totalAttendance = dataSnapshot.getValue(String.class);
                        if (totalAttendance != null) {
                            binding.attendance.setText(totalAttendance);
                            ref.child("course students")
                                    .child(Objects.requireNonNull(auth.getCurrentUser())
                                            .getUid())
                                    .child(courseId)
                                    .child("attendanceTotal")
                                    .setValue(totalAttendance);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error here
                    }
                });
    }

    private void getStGrade(String courseId) {
        DatabaseReference stGradeRef = ref.child("course students")
                .child(userId)
                .child(courseId)
                .child("stGrade")
                .child("stGrade");

        stGradeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long stGrade = dataSnapshot.getValue(Long.class);
                binding.stGrade.setText(String.valueOf(stGrade));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error here
            }
        });
    }

    private void getQuizGrade(String courseId) {

        DatabaseReference quizGradeRef = ref.child("course students")
                .child(userId)
                .child(courseId)
                .child("quizGrade")
                .child("quizGrade");

        quizGradeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long quizGrade = dataSnapshot.getValue(Long.class);
                binding.grade.setText(String.valueOf(quizGrade));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error here
            }
        });
    }


}
