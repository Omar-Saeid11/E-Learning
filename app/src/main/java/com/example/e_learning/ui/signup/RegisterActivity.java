package com.example.e_learning.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.R;
import com.example.e_learning.data.User;
import com.example.e_learning.databinding.ActivityRegisterBinding;
import com.example.e_learning.ui.doctor.DoctorActivity;
import com.example.e_learning.ui.login.LoginActivity;
import com.example.e_learning.ui.student.StudentActivity;
import com.example.e_learning.util.FingerprintHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements FingerprintHelper.FirebaseAuthenticationListener {
    ActivityRegisterBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    private FingerprintHelper fingerprintHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String userType = intent.getStringExtra("userType");
        fingerprintHelper = new FingerprintHelper(this, this);

        binding.gotoLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        binding.btnCreate.setOnClickListener(v -> {
            String name = Objects.requireNonNull(binding.name.getEditText()).getText().toString().trim();
            String email = Objects.requireNonNull(binding.email.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(binding.password.getEditText()).getText().toString();
            validate(name, email, password, userType);
        });

        binding.fingerprint.setOnClickListener(v -> {
            if (fingerprintHelper.isFingerprintAuthAvailable()) {
                fingerprintHelper.startAuth();
            }
        });
    }

    private void validate(String name, String email, String password, String userType) {
        if (name.isEmpty()) {
            binding.name.setError(getString(R.string.required));
            binding.fingerprint.setEnabled(false);
        } else if (email.isEmpty()) {
            binding.email.setError(getString(R.string.required));
            binding.fingerprint.setEnabled(false);
        } else if (password.isEmpty() || password.length() < 7) {
            binding.password.setError(getString(R.string.required));
        } else {
            register(name, email, password, userType);
            binding.fingerprint.setEnabled(true);
        }
    }

    private void register(String name, String email, String password, String userType) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(
                        this,
                        authResult ->
                                sendDataToRealtime(
                                        name,
                                        email,
                                        Objects.requireNonNull(authResult.getUser()).getUid(), userType))
                .addOnFailureListener(this,
                        e -> Toast.makeText(
                                RegisterActivity.this,
                                e.getLocalizedMessage(),
                                Toast.LENGTH_LONG
                        ).show());
    }

    private void sendDataToRealtime(String name, String email, String userId, String userType) {
        ref.child("users").child(userId).setValue(new User(name, email, userId, userType))
                .addOnSuccessListener(this, unused -> {
                    if (userType.equals("doctor")) {
                        startActivity(new Intent(RegisterActivity.this, DoctorActivity.class));
                        finish();
                    } else {
                        Intent intent = new Intent(RegisterActivity.this, StudentActivity.class);
                        intent.putExtra("studentName", name);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(this,
                        e -> Toast.makeText(
                                RegisterActivity.this,
                                e.getLocalizedMessage(),
                                Toast.LENGTH_LONG
                        ).show());
    }

    @Override
    public void onAuthenticationSuccess() {
        String name = Objects.requireNonNull(binding.name.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(binding.email.getEditText()).getText().toString().trim();
        if (Objects.equals(getIntent().getStringExtra("userType"), "doctor")) {
            if (name.isEmpty()) {
                binding.name.setError(getString(R.string.required));
                binding.fingerprint.setEnabled(false);
            } else if (email.isEmpty()) {
                binding.email.setError(getString(R.string.required));
                binding.fingerprint.setEnabled(false);
            } else {
                startActivity(new Intent(this, DoctorActivity.class));
                finish();
            }
        } else {
            if (name.isEmpty()) {
                binding.name.setError(getString(R.string.required));
                binding.fingerprint.setEnabled(false);
            } else if (email.isEmpty()) {
                binding.email.setError(getString(R.string.required));
                binding.fingerprint.setEnabled(false);
            } else {
                startActivity(new Intent(this, StudentActivity.class));
                finish();
            }
        }
    }
}
