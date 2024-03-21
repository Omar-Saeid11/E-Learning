package com.example.e_learning.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.R;
import com.example.e_learning.util.UserPreferences;
import com.example.e_learning.data.Chat;
import com.example.e_learning.databinding.ActivityStudentChatCourseBinding;
import com.example.e_learning.ui.adapter.AdapterItemChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class StudentChatCourse extends AppCompatActivity {
    ActivityStudentChatCourseBinding binding;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    AdapterItemChat adapterItemChat = new AdapterItemChat();
    private ArrayList<Chat> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentChatCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String courseId = intent.getStringExtra("courseId");
        UserPreferences userPreferences = new UserPreferences(this);
        String name = userPreferences.getStudentName();
        getMessage(courseId);
        binding.materialButton.setOnClickListener(v -> {
            String message = binding.messageEditText.getText().toString().trim();
            if (message.isEmpty()) {
                binding.messageEditText.setError(getText(R.string.required));
            } else {
                sendMessage(courseId, name, message);
                binding.messageEditText.setText("");
            }
        });
    }

    private void sendMessage(String courseId, String name, String message) {
        String chatId = ref.push().getKey();
        assert chatId != null;
        ref.child("chats").child(courseId).child(chatId)
                .setValue(new Chat(message, Objects.requireNonNull(auth.getCurrentUser()).getUid(), name));
    }

    private void getMessage(String courseId) {
        ref.child("chats").child(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    list.add(dataSnapshot.getValue(Chat.class));
                }
                adapterItemChat.setList(list);
                binding.recyclerChats.setAdapter(adapterItemChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}