package com.example.e_learning.ui.get_start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.databinding.ActivityGetStartBinding;
import com.example.e_learning.ui.login.LoginActivity;
import com.example.e_learning.util.UserPreferences;

public class GetStartActivity extends AppCompatActivity {
    ActivityGetStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserPreferences userPreferences = new UserPreferences(this);
        binding.student.setOnClickListener(v -> {
            userPreferences.setUserType("student");
            Intent intent = new Intent(GetStartActivity.this, LoginActivity.class);
            intent.putExtra("userType", "student");
            startActivity(intent);
        });
        binding.doctor.setOnClickListener(v -> {
            userPreferences.setUserType("doctor");
            Intent intent = new Intent(GetStartActivity.this, LoginActivity.class);
            intent.putExtra("userType", "doctor");
            startActivity(intent);
        });
    }
}