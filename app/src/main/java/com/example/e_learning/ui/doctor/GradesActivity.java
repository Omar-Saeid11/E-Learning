package com.example.e_learning.ui.doctor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.data.InfoStudent;
import com.example.e_learning.databinding.ActivityGradesBinding;
import com.example.e_learning.ui.adapter.AdapterItemGrades;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GradesActivity extends AppCompatActivity {
    ActivityGradesBinding binding;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    FirebaseAuth auth=FirebaseAuth.getInstance();
    ArrayList<InfoStudent> list = new ArrayList<>();
    AdapterItemGrades adapterItemGrades = new AdapterItemGrades();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGradesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String courseId = intent.getStringExtra("courseId");
        getGrade(courseId);
    }

    private void getGrade(String courseId) {
        ref.child("courses")
                .child(courseId)
                .child("course students")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    list.add(snapshot1.getValue(InfoStudent.class));
                }
                adapterItemGrades.setList(list);
                binding.recyclerGrades.setAdapter(adapterItemGrades);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}