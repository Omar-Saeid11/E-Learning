package com.example.e_learning.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.R;
import com.example.e_learning.data.Course;
import com.example.e_learning.data.InfoStudent;
import com.example.e_learning.databinding.ActivityStudentBinding;
import com.example.e_learning.ui.adapter.AdapterItemSubject;
import com.example.e_learning.ui.get_start.GetStartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class StudentActivity extends AppCompatActivity {
    private ActivityStudentBinding binding;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private ArrayList<Course> list;
    private AdapterItemSubject adapterItemSubject;
    private String studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        adapterItemSubject = new AdapterItemSubject();

        getCourses();

        binding.addMaterial.setOnClickListener(v -> {
            String courseId = Objects.requireNonNull(binding.enterCourse.getEditText()).getText().toString().trim();
            if (courseId.isEmpty()) {
                binding.enterCourse.setError(getString(R.string.required));
            } else {
                checkJoinToCourse(courseId);
            }
        });

        binding.logout.setOnClickListener(view -> {
            auth.signOut();
            Intent intent = new Intent(StudentActivity.this, GetStartActivity.class);
            startActivity(intent);
        });

        adapterItemSubject.setOnItemClick(course -> {
            Intent intent = new Intent(StudentActivity.this, StudentControlActivity.class);
            intent.putExtra("courseName", course.getCourseName());
            intent.putExtra("courseId", course.getCourseId());
            intent.putExtra("studentName", studentName);
            startActivity(intent);
        });
    }

    private void getCourses() {
        ref.child("course students")
                .child(Objects.requireNonNull(auth.getCurrentUser()).getUid().trim())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear(); // Clear the list before adding courses
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            list.add(snapshot1.getValue(Course.class));
                        }
                        adapterItemSubject.setList(list);
                        binding.recyclerSubjects.setAdapter(adapterItemSubject);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StudentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkJoinToCourse(String courseId) {
        Intent intent = getIntent();
        studentName = intent.getStringExtra("studentName");
        String studentId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        // Check if the student is already registered in the course
        ref.child("course students").child(studentId).child(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(StudentActivity.this, "You are already registered in this course", Toast.LENGTH_SHORT).show();
                } else {
                    // Course not found, add the student to the course
                    ref.child("courses").child(courseId).child("courseName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                String courseName = snapshot.getValue().toString();
                                addStudent(studentName, studentId, courseId, courseName);
                                Objects.requireNonNull(binding.enterCourse.getEditText()).setText("");
                                binding.enterCourse.clearFocus();
                            } else {
                                Toast.makeText(StudentActivity.this, "Course not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(StudentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addStudent(String studentName, String studentId, String courseId, String courseName) {
        InfoStudent grade = new InfoStudent();
        grade.setStudentId(studentId);
        grade.setCourseName(courseName);
        grade.setStudentName(studentName);
        grade.setCourseId(courseId);
        HashMap<String, Integer> stGrade = new HashMap<>();
        stGrade.put("stGrade", 0);
        grade.setStGrade(stGrade);

        HashMap<String, Integer> quizGrade = new HashMap<>();
        quizGrade.put("quizGrade", 0);
        grade.setQuizGrade(quizGrade);
        grade.setAttendance("0");
        grade.setAttendanceTotal("0");


        ref.child("course students").child(studentId).child(courseId).setValue(grade)
                .addOnSuccessListener(unused ->
                        Toast.makeText(
                                StudentActivity.this,
                                "Done",
                                Toast.LENGTH_SHORT
                        ).show())
                .addOnFailureListener(e ->
                        Toast.makeText(
                                StudentActivity.this,
                                e.getLocalizedMessage(),
                                Toast.LENGTH_SHORT
                        ).show());

        ref.child("courses").child(courseId).child("course students")
                .child(studentId)
                .setValue(grade)
                .addOnSuccessListener(unused ->
                        Toast.makeText(
                                StudentActivity.this,
                                "Done",
                                Toast.LENGTH_SHORT
                        ).show())
                .addOnFailureListener(e ->
                        Toast.makeText(
                                StudentActivity.this,
                                e.getLocalizedMessage(),
                                Toast.LENGTH_SHORT
                        ).show());
    }


}