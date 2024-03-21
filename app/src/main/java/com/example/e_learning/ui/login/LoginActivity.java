package com.example.e_learning.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.R;
import com.example.e_learning.databinding.ActivityLoginBinding;
import com.example.e_learning.ui.doctor.DoctorActivity;
import com.example.e_learning.ui.signup.RegisterActivity;
import com.example.e_learning.ui.student.StudentActivity;
import com.example.e_learning.util.FingerprintHelper;
import com.example.e_learning.util.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements FingerprintHelper.FirebaseAuthenticationListener {
    ActivityLoginBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private FingerprintHelper fingerprintHelper;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent userType = getIntent();
        binding.btnSignIn.setOnClickListener(v -> {
            String email = Objects.requireNonNull(binding.email.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(binding.password.getEditText()).getText().toString();
            validate(email, password);
        });
        binding.gotoRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("userType", userType.getStringExtra("userType"));
            startActivity(intent);
        });

        fingerprintHelper = new FingerprintHelper(this, this);
        binding.fingerprint.setOnClickListener(v -> {
            if (fingerprintHelper.isFingerprintAuthAvailable()) {
                fingerprintHelper.startAuth();
            }
        });
    }

    private void validate(String email, String password) {
        if (email.isEmpty()) {
            binding.email.setError(getText(R.string.required));
            binding.fingerprint.setEnabled(false);
        } else if (password.isEmpty()) {
            binding.password.setError(getText(R.string.required));
        } else {
            login(email, password);
        }
    }

    private void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            String userType = getIntent().getStringExtra("userType");
            assert userType != null;
            if (userType.equals("doctor")) {
                checkIfUserIsDoctor(Objects.requireNonNull(authResult.getUser()).getUid());
            } else if (userType.equals("student")) {
                checkIfUserIsStudent(Objects.requireNonNull(authResult.getUser()).getUid());
            }
        }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
    }

    private void checkIfUserIsDoctor(String userId) {
        ref.child("users").child(userId).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userType = dataSnapshot.getValue(String.class);
                if (userType != null && userType.equals("doctor")) {
                    startActivity(new Intent(LoginActivity.this, DoctorActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "You are not authorized to access this activity.", Toast.LENGTH_LONG).show();
                    auth.signOut();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkIfUserIsStudent(String userId) {
        ref.child("users").child(userId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.getValue(String.class);
                UserPreferences userPreferences = new UserPreferences(LoginActivity.this);
                userPreferences.setStudentName(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child("users").child(userId).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userType = dataSnapshot.getValue(String.class);
                if (userType != null && userType.equals("student")) {
                    Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
                    intent.putExtra("studentName", name);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "You are not authorized to access this activity.", Toast.LENGTH_LONG).show();
                    auth.signOut();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAuthenticationSuccess() {
        binding.fingerprint.setEnabled(true);
        String email = Objects.requireNonNull(binding.email.getEditText()).getText().toString().trim();
        if (Objects.equals(getIntent().getStringExtra("userType"), "doctor")) {
            if (email.isEmpty()) {
                binding.email.setError(getString(R.string.required));
                binding.fingerprint.setEnabled(false);
            } else {
                startActivity(new Intent(this, DoctorActivity.class));
                finish();
            }
        } else {
            if (email.isEmpty()) {
                binding.email.setError(getString(R.string.required));
                binding.fingerprint.setEnabled(false);
            } else {
                startActivity(new Intent(this, StudentActivity.class));
                finish();
            }
        }
    }

}