package com.example.fypcommunicationtool;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class ApplyCertificateFragment extends Fragment {

    Button selectButton, uploadButton;
    ListView listView;
    TextView notification;

    private Uri filepath; //Uri = URL for local storage
    //    ProgressBar progressBar;
    ProgressDialog progressDialog;

    FirebaseStorage storage;
    FirebaseDatabase database;

    public ApplyCertificateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_apply_certificate, container, false);
        contactInfo(v);
        initButton(v);
        return v;
    }

    void contactInfo(View view) {

        EditText address = view.findViewById(R.id.addressField);
        EditText city = view.findViewById(R.id.cityField);
        EditText postcode = view.findViewById(R.id.postcodeField);
        EditText phoneNumber = view.findViewById(R.id.phoneNumberField);
        Spinner state = view.findViewById(R.id.stateSpinner);
        Button applyButton = view.findViewById(R.id.applyButton);

        toDatabase(address, city, postcode, phoneNumber, state, applyButton);
    }

    private void toDatabase(final EditText address, final EditText city, final EditText postcode, final EditText phoneNumber, Spinner state, Button applyButton) {

        final spinnerClass spinner = new spinnerClass(getContext(), state);
        spinner.spinnerActivity();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("CertApplication_StudentInfo");

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressField = address.getText().toString();
                String cityField = city.getText().toString();
                String postcodeField = postcode.getText().toString();
                String phoneNumberField = phoneNumber.getText().toString();
                String stateField = spinner.getItem();

                applyCertContactInfo info = new applyCertContactInfo(addressField, cityField, postcodeField, phoneNumberField, stateField);
                info.setAddress(addressField);
                info.setCity(cityField);
                info.setPhoneNumber(phoneNumberField);
                info.setPostcode(postcodeField);
                info.setState(stateField);
                myRef.push().setValue(info);
            }
        });
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setTitle("Apply Certificate");
    }

    void initButton(View view) {
        selectButton = view.findViewById(R.id.selectReceipt);
        uploadButton = view.findViewById(R.id.uploadReceipt);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        notification = view.findViewById(R.id.notification);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                }
                else
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 15);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filepath!=null) //User has selected a file
                    uploadFile(filepath);
                else
                    Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT); //to fetch files
        startActivityForResult(intent, 20);
    }

    //To check if user successfully select file
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK && data != null) {
            filepath = data.getData(); //return uri of selected file
            notification.setText("File has been selected: " + data.getData().getLastPathSegment());
        } else {
            Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "Please provide permission", Toast.LENGTH_SHORT).show();
    }

    //Upload pdf file
    private void uploadFile(Uri filepath) {

        progressDialog = new ProgressDialog(getContext());
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
                            Toast.makeText(getContext(), "File submission successful", Toast.LENGTH_SHORT).show();
                            notification.setText("File has been successfully uploaded: "+fileName);
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "File submission failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "File submission failed", Toast.LENGTH_SHORT).show();
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