package com.example.e_learning.ui.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.R;
import com.example.e_learning.data.Quiz;
import com.example.e_learning.databinding.ActivityQuizBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {
    ActivityQuizBinding binding;
    private final ArrayList<Quiz> quizList = new ArrayList<>();

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private int rightAnswer = 0;
    private String quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String courseId = intent.getStringExtra("courseId");
        assert courseId != null;
        quizId = ref.child("quiz").child(courseId).push().getKey();
        binding.checkBoxOne.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rightAnswer = 1;
                binding.checkBoxTwo.setChecked(false);
                binding.checkBoxThree.setChecked(false);
                binding.checkBoxFour.setChecked(false);
                binding.checkBoxOne.setClickable(false);

            }
        });
        binding.checkBoxTwo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rightAnswer = 2;
                binding.checkBoxOne.setChecked(false);
                binding.checkBoxThree.setChecked(false);
                binding.checkBoxFour.setChecked(false);
                binding.checkBoxTwo.setClickable(false);

            }
        });
        binding.checkBoxThree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rightAnswer = 3;
                binding.checkBoxTwo.setChecked(false);
                binding.checkBoxOne.setChecked(false);
                binding.checkBoxFour.setChecked(false);
                binding.checkBoxThree.setClickable(false);

            }
        });
        binding.checkBoxFour.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rightAnswer = 4;
                binding.checkBoxTwo.setChecked(false);
                binding.checkBoxThree.setChecked(false);
                binding.checkBoxOne.setChecked(false);
                binding.checkBoxFour.setClickable(false);

            }
        });
        binding.btnNextQuestion.setOnClickListener(v -> {
            String question = Objects.requireNonNull(binding.question.getEditText()).getText().toString();
            String answerOne = Objects.requireNonNull(binding.one.getEditText()).getText().toString();
            String answerTwo = Objects.requireNonNull(binding.two.getEditText()).getText().toString();
            String answerThree = Objects.requireNonNull(binding.three.getEditText()).getText().toString();
            String answerFour = Objects.requireNonNull(binding.four.getEditText()).getText().toString();
            validate(question, answerOne, answerTwo, answerThree, answerFour, rightAnswer);
        });
        binding.btnUploadFile.setOnClickListener(v -> {
            if (quizList.isEmpty()) {
                Toast.makeText(QuizActivity.this, "You should add at least one question", Toast.LENGTH_SHORT).show();
            } else {
                createQuiz(courseId, quizList);
            }
        });
    }

    private void validate(String question, String answerOne, String answerTwo, String answerThree, String answerFour, int rightAnswer) {
        if (question.isEmpty()) {
            binding.question.setError(getText(R.string.required));
        } else if (answerOne.isEmpty()) {
            binding.one.setError(getText(R.string.required));
        } else if (answerTwo.isEmpty()) {
            binding.two.setError(getText(R.string.required));
        } else if (answerThree.isEmpty()) {
            binding.three.setError(getText(R.string.required));
        } else if (answerFour.isEmpty()) {
            binding.four.setError(getText(R.string.required));
        } else if (rightAnswer == 0) {
            Toast.makeText(this, "Please Select Right Answer", Toast.LENGTH_SHORT).show();
        } else {
            quizList.add(new Quiz(question, answerOne, answerTwo, answerThree, answerFour, rightAnswer));
            Objects.requireNonNull(binding.question.getEditText()).setText("");
            Objects.requireNonNull(binding.one.getEditText()).setText("");
            Objects.requireNonNull(binding.two.getEditText()).setText("");
            Objects.requireNonNull(binding.three.getEditText()).setText("");
            Objects.requireNonNull(binding.four.getEditText()).setText("");
        }
    }

    private void createQuiz(String courseId, ArrayList<Quiz> quizList) {
        ref.child("quiz")
                .child(courseId)
                .child(quizId)
                .setValue(quizList)
                .addOnSuccessListener(unused ->
                        Toast.makeText(
                                QuizActivity.this,
                                "Uploaded",
                                Toast.LENGTH_SHORT
                        ).show())
                .addOnFailureListener(e -> Toast.makeText(
                        QuizActivity.this,
                        e.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show());
    }
}