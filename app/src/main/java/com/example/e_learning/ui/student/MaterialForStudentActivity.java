package com.example.e_learning.ui.student;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.databinding.ActivityMaterialForStudentBinding;
import com.example.e_learning.ui.adapter.AdapterItemMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MaterialForStudentActivity extends AppCompatActivity {
    ActivityMaterialForStudentBinding binding;
    private ArrayList<String> list = new ArrayList<>();
    AdapterItemMaterial adapterItemMaterial = new AdapterItemMaterial();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaterialForStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String courseId = intent.getStringExtra("courseId");
        getMaterial(courseId);
        adapterItemMaterial.setOnItemClick(this::openPdf);
    }

    private void getMaterial(String courseId) {
        DatabaseReference materialsRef = FirebaseDatabase.getInstance().getReference();
        materialsRef.child("pdfs").child(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list.add(snapshot.getValue(String.class));
                }
                adapterItemMaterial.setList(list);
                binding.recyclerSubjects.setAdapter(adapterItemMaterial);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }

    private void openPdf(String url) {
        Intent downloadIntent = new Intent(Intent.ACTION_VIEW);
        downloadIntent.setDataAndType(Uri.parse(url), "application/pdf");
        downloadIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent openIntent = new Intent(Intent.ACTION_VIEW);
        openIntent.setDataAndType(Uri.parse(url), "application/pdf");

        Intent chooserIntent = Intent.createChooser(downloadIntent, "Download or open PDF");
        chooserIntent.putExtra(Intent.EXTRA_INTENT, openIntent);

        try {
            startActivity(chooserIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
