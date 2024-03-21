package com.example.e_learning.ui.doctor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learning.databinding.ActivityUploadMaterialBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UploadMaterialActivity extends AppCompatActivity {
    ActivityUploadMaterialBinding binding;
    private static final int PICK_PDF_REQUEST = 1;
    private Uri selectedPdfUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadMaterialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        courseId = getIntent().getStringExtra("courseId");

        binding.addMaterial.setOnClickListener(v -> selectPdfFile());

        binding.btnUploadFile.setOnClickListener(v -> uploadPdfFile());
    }

    private void selectPdfFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    private void uploadPdfFile() {
        if (selectedPdfUri != null) {
            StorageReference fileReference = storageReference.child("pdfs/" + courseId + "/" + System.currentTimeMillis() + ".pdf");
            fileReference.putFile(selectedPdfUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(UploadMaterialActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
//                            savePdfFileInfoToDatabase(taskSnapshot.getStorage().getDownloadUrl().toString());
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String pdfPath = uri.toString();
                            String idPdf = databaseReference.push().getKey();
                            assert idPdf != null;
                            databaseReference.child("pdfs").child(courseId).child(idPdf).setValue(pdfPath);
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(UploadMaterialActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

//    private void savePdfFileInfoToDatabase(String downloadUrl) {
//        String idPdf = databaseReference.push().getKey();
//        assert idPdf != null;
//        DatabaseReference newPdfRef = databaseReference.child("pdfs").child(courseId).child(idPdf);
//        newPdfRef.child("url").setValue(downloadUrl);
//        newPdfRef.child("timestamp").setValue(String.valueOf(System.currentTimeMillis()));
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedPdfUri = data.getData();
            Toast.makeText(this, "PDF file selected", Toast.LENGTH_SHORT).show();
        }
    }
}