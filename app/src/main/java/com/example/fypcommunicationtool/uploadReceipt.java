package com.example.fypcommunicationtool;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class uploadReceipt extends ApplyCertificateActivity {

    Button selectButton, uploadButton;
    ListView listView;
    TextView notification;

    private Uri filepath; //Uri = URL for local storage
    //    ProgressBar progressBar;
    ProgressDialog progressDialog;

    FirebaseStorage storage;
    FirebaseDatabase database;
    Activity activity;
    View view;

    public uploadReceipt(Activity activity,View view) {
        super();
        this.activity = activity;
        this.view = view;
    }

    public uploadReceipt(Activity activity) {
        super();
        this.activity = activity;
    }

    void initButton() {
//        selectButton = view.findViewById(R.id.selectReceipt);
//        uploadButton = view.findViewById(R.id.uploadReceipt);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        notification = view.findViewById(R.id.notification);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                }
                else
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 15);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filepath!=null) //User has selected a file
                    uploadFile(filepath);
                else
                    Toast.makeText(activity, "Please select a file", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT); //to fetch files
        activity.startActivityForResult(intent, 20);
    }

    //To check if user successfully select file
//    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK && data != null) {
            filepath = data.getData(); //return uri of selected file
            notification.setText("File has been selected: " + data.getData().getLastPathSegment());
        } else {
            Toast.makeText(activity, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    //Method to acknowledge permission, Override means automatically called  by Android
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==15 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        }
        else
            Toast.makeText(activity, "Please provide permission", Toast.LENGTH_SHORT).show();
    }

    //Upload pdf file
    private void uploadFile(Uri filepath) {

        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        //Store file in storage
        final String fileName = System.currentTimeMillis()+"_Coursework";
        StorageReference mStorageRef = storage.getReference().child("StudentCoursework").child("Coursework_Level1").child("Coursework_Submission");
        mStorageRef.child(fileName).putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                DatabaseReference databaseReference = database.getReference().child("STUDENT_COURSEWORK").child("COURSEWORK_LEVEL1_ANSWER");
                databaseReference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "File submission successful", Toast.LENGTH_SHORT).show();
//                            notification.setText("File has been successfully uploaded: "+fileName);
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "File submission failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "File submission failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                //track progress of upload
                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }
}
