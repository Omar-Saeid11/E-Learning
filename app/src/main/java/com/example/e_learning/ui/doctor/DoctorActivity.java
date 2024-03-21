package com.example.e_learning.ui.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.data.Course;
import com.example.e_learning.databinding.ActivityDoctorBinding;
import com.example.e_learning.ui.adapter.AdapterItemSubject;
import com.example.e_learning.ui.get_start.GetStartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorActivity extends AppCompatActivity {
    ActivityDoctorBinding binding;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    ArrayList<Course> list = new ArrayList<>();
    AdapterItemSubject adapterUser = new AdapterItemSubject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
        binding.addMaterial.setOnClickListener(v -> {
//                startActivity(new Intent(DoctorActivity.this, CreateCourse.class));
//                finish();
            Intent intent = new Intent(DoctorActivity.this, CreateCourse.class);
            startActivity(intent);
        });
        adapterUser.setOnItemClick(course -> {
            Intent intent = new Intent(DoctorActivity.this, DoctorControlPage.class);
            intent.putExtra("courseId", course.getCourseId());
            startActivity(intent);
        });

        binding.logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(DoctorActivity.this, GetStartActivity.class));
            finish();
        });
    }

    private void getData() {
        String currentUserId = FirebaseAuth.getInstance().getUid();
        ref.child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Course course = snapshot1.getValue(Course.class);
                    assert course != null;
                    if (course.getDocId().equals(currentUserId)) {
                        list.add(course);
                    }
                }
                adapterUser.setList(list);
                binding.recyclerSubjects.setAdapter(adapterUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(
                        DoctorActivity.this, "Error retrieving data: "
                                + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}