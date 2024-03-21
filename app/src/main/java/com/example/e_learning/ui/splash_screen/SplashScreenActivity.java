package com.example.e_learning.ui.splash_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.ui.get_start.GetStartActivity;
import com.example.e_learning.databinding.ActivitySplashScreenBinding;
import com.example.e_learning.ui.doctor.DoctorActivity;
import com.example.e_learning.ui.student.StudentActivity;
import com.example.e_learning.util.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        UserPreferences userPreferences = new UserPreferences(this);
        String userType = userPreferences.getUserType();

        new Handler().postDelayed(() -> {
            if (auth.getUid() == null) {
                startActivity(new Intent(SplashScreenActivity.this, GetStartActivity.class));
                finish();
            } else {
                if (userType.equals("doctor")) {
                    startActivity(new Intent(SplashScreenActivity.this, DoctorActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, StudentActivity.class));
                    finish();
                }
            }
        }, 3000);
    }
}