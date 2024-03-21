package com.example.e_learning.ui.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.util.UserPreferences;
import com.example.e_learning.data.InfoStudent;
import com.example.e_learning.databinding.ActivityAttendanceBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class AttendanceActivity extends AppCompatActivity {
    ActivityAttendanceBinding binding;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    String codeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String courseId = intent.getStringExtra("courseId");
        binding.btnGenerateCode.setOnClickListener(v -> {
            Random random = new Random();
            int code = random.nextInt(9000) + 1000;
            codeString = code + "";
            binding.codeAttendance.setText(codeString);
            binding.btnConfirm.setEnabled(true);
        });
        binding.btnConfirm.setOnClickListener(v -> attendance(courseId, codeString));
    }

    private void attendance(String courseId, String code) {
        ref.child("attendance").child(courseId).child(code + "").setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Increment the attendanceTotal for the course
                ref.child("courses").child(courseId).child("course students").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            InfoStudent grade = snapshot.getValue(InfoStudent.class);
                            if (grade != null) {
                                int attendanceTotal = Integer.parseInt(grade.getAttendanceTotal());
                                attendanceTotal++;
                                grade.setAttendanceTotal(String.valueOf(attendanceTotal));
                                snapshot.getRef().setValue(grade);
//                                userPreferences.setKeyTotalAttendance(String.valueOf(attendanceTotal));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AttendanceActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(AttendanceActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(AttendanceActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

}