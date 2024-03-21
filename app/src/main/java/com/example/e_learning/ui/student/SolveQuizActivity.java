package com.example.e_learning.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.R;
import com.example.e_learning.data.Quiz;
import com.example.e_learning.data.QuizAnswer;
import com.example.e_learning.databinding.ActivitySolveQuizBinding;
import com.example.e_learning.util.UserPreferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SolveQuizActivity extends AppCompatActivity {
    ActivitySolveQuizBinding binding;
    private int rightAnswer = 0;
    private int grade = 0;
    private int position = 0;
    private String courseId, quizId;
    private List<Quiz> list = new ArrayList<>();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySolveQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        courseId = intent.getStringExtra("courseId");
        quizId = intent.getStringExtra("quizId");
        binding.btnNext.setOnClickListener(view -> {
            if (rightAnswer != 0) {
                checkAnswer();
            }
        });

        binding.answerOne.setOnClickListener(view -> {
            rightAnswer = 1;
            binding.answerOne.setBackgroundResource(R.drawable.background_new_course);
            binding.answerTwo.setBackgroundResource(R.drawable.background_solve_quiz_items);
            binding.answerThree.setBackgroundResource(R.drawable.background_solve_quiz_items);
            binding.answerFour.setBackgroundResource(R.drawable.background_solve_quiz_items);
        });

        binding.answerTwo.setOnClickListener(view -> {
            rightAnswer = 2;
            binding.answerTwo.setBackgroundResource(R.drawable.background_new_course);
            binding.answerOne.setBackgroundResource(R.drawable.background_solve_quiz_items);
            binding.answerThree.setBackgroundResource(R.drawable.background_solve_quiz_items);
            binding.answerFour.setBackgroundResource(R.drawable.background_solve_quiz_items);
        });

        binding.answerThree.setOnClickListener(view -> {
            rightAnswer = 3;
            binding.answerThree.setBackgroundResource(R.drawable.background_new_course);
            binding.answerOne.setBackgroundResource(R.drawable.background_solve_quiz_items);
            binding.answerTwo.setBackgroundResource(R.drawable.background_solve_quiz_items);
            binding.answerFour.setBackgroundResource(R.drawable.background_solve_quiz_items);
        });

        binding.answerFour.setOnClickListener(view -> {
            rightAnswer = 4;
            binding.answerFour.setBackgroundResource(R.drawable.background_new_course);
            binding.answerOne.setBackgroundResource(R.drawable.background_solve_quiz_items);
            binding.answerTwo.setBackgroundResource(R.drawable.background_solve_quiz_items);
            binding.answerThree.setBackgroundResource(R.drawable.background_solve_quiz_items);
        });

        getQuiz(courseId, quizId);
    }

    private void getQuiz(String courseId, String quizId) {
        ref.child("quiz").child(courseId).child(quizId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.getValue() != null) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        list.add(snapshot1.getValue(Quiz.class));
                    }
                    initView();
                } else {
                    Toast.makeText(SolveQuizActivity.this, "No Quiz", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SolveQuizActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        if (position == (list.size() - 1)) {
            binding.btnNext.setText("Finish");
        }
        binding.question.setText(list.get(position).getQuestion());
        binding.answerOne.setText(list.get(position).getChooseOne());
        binding.answerTwo.setText(list.get(position).getChooseTwo());
        binding.answerThree.setText(list.get(position).getChooseThree());
        binding.answerFour.setText(list.get(position).getChooseFour());
    }

    private void checkAnswer() {
        if (list.get(position).getRightAnswer() == rightAnswer) {
            grade++;
        }
        if (position == (list.size() - 1)) {
            // Finish quiz
            uploadResult(courseId, quizId, grade);
            Toast.makeText(this, "Your Answer has been uploaded", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            position++;
            initView();
        }
        resetAnswer();
    }

    private void uploadResult(String courseId, String quizId, int grade) {
        UserPreferences userPreferences = new UserPreferences(this);
        ref.child("quiz answer").child(courseId).child(quizId)
                .child(Objects.requireNonNull(auth.getUid())).setValue(new QuizAnswer(userPreferences.getStudentName(),
                        auth.getUid(), grade));
        ref.child("course students")
                .child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .child(courseId)
                .child("stGrade")
                .child("stGrade")
                .setValue(grade);
        ref.child("course students")
                .child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .child(courseId)
                .child("quizGrade")
                .child("quizGrade")
                .setValue(list.size());


        // Save the grade to the quizGrade field in the database
        ref.child("courses").child(courseId).child("course students")
                .child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .child("stGrade").child("stGrade").setValue(grade).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SolveQuizActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        ref.child("courses").child(courseId).child("course students")
                .child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .child("quizGrade").child("quizGrade").setValue(list.size()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SolveQuizActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void resetAnswer() {
        rightAnswer = 0;
        binding.answerOne.setBackgroundResource(R.drawable.background_solve_quiz_items);
        binding.answerTwo.setBackgroundResource(R.drawable.background_solve_quiz_items);
        binding.answerThree.setBackgroundResource(R.drawable.background_solve_quiz_items);
        binding.answerFour.setBackgroundResource(R.drawable.background_solve_quiz_items);
    }
}
