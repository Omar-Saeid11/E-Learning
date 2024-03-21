package com.example.e_learning.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.databinding.ActivityCourseQuizsFroStudentBinding;
import com.example.e_learning.ui.adapter.AdapterItemQuiz;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CourseQuizFroStudentActivity extends AppCompatActivity {
    ActivityCourseQuizsFroStudentBinding binding;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private ArrayList<String> list = new ArrayList<>();
    AdapterItemQuiz adapterItemQuiz = new AdapterItemQuiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseQuizsFroStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String courseId = intent.getStringExtra("courseId");
        getQuizzes(courseId);
        adapterItemQuiz.setOnItemClick(quizId -> checkIfUserAnswered(courseId, quizId));
    }

    private void getQuizzes(String courseId) {
        ref.child("quiz").child(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    list.add(snapshot1.getKey());
                }
                adapterItemQuiz.setList(list);
                binding.recyclerSubjects.setAdapter(adapterItemQuiz);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CourseQuizFroStudentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfUserAnswered(String courseId, String quizId) {
        ref.child("quiz answers").child(courseId).child(quizId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(Objects.requireNonNull(auth.getUid()))) {
                    Toast.makeText(
                            CourseQuizFroStudentActivity.this,
                            "You have already answered",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    ref.child("quiz answers").child(courseId).child(quizId).child(auth.getUid()).setValue(true);
                    Intent intent = new Intent(CourseQuizFroStudentActivity.this, SolveQuizActivity.class);
                    intent.putExtra("courseId", courseId);
                    intent.putExtra("quizId", quizId);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CourseQuizFroStudentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
