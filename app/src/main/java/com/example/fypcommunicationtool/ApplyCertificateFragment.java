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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class ApplyCertificateFragment extends Fragment {

    //    Button selectButton;
    Button uploadButton;
    TextView notification;
    RadioButton checkInfoButton;

    private Uri filepath; //Uri = URL for local storage
    ProgressDialog progressDialog;

    FirebaseStorage storage;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private Boolean isUploadFinished = false;

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
        //TODO: async task
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

        checkInfoButton = view.findViewById(R.id.check_cert_info_button);
        toDatabase(address, city, postcode, phoneNumber, state, applyButton, checkInfoButton);
    }

    private void toDatabase(final EditText address, final EditText city, final EditText postcode,
                            final EditText phoneNumber, Spinner state, Button applyButton, RadioButton checkInfoButton) {

        final spinnerClass spinner = new spinnerClass(getContext(), state);
        spinner.spinnerActivity();

        final DatabaseReference myRef = database.getReference().child("CertApplication_StudentInfo").child("NewApplication");

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInfoButton.isChecked() && isUploadFinished == true) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    String userId = currentUser.getUid();
                    String addressField = address.getText().toString();
                    String cityField = city.getText().toString();
                    String postcodeField = postcode.getText().toString();
                    String phoneNumberField = phoneNumber.getText().toString();
                    String stateField = spinner.getItem();

                    ApplyCertContactInfo info = new ApplyCertContactInfo();
//                    ApplyCertContactInfo info = new ApplyCertContactInfo(addressField, cityField, postcodeField, phoneNumberField, stateField);
                    HashMap<String, Object> values = new HashMap<>();
                    values.put("address", addressField);
                    values.put("city", cityField);
                    values.put("phoneNumber", phoneNumberField);
                    values.put("postcode", postcodeField);
                    values.put("state", stateField);

//                    info.setAddress(addressField);
//                    info.setCity(cityField);
//                    info.setPhoneNumber(phoneNumberField);
//                    info.setPostcode(postcodeField);
//                    info.setState(stateField);

                    DatabaseReference reference = database.getReference().child("Users").child(userId);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            myRef.updateChildren(values);
                            String name = snapshot.child("fullName").getValue().toString();
                            myRef.child(userId).child("name").setValue(name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("msg", "Error: " + error);
                        }
                    });


                    myRef.child(userId).updateChildren(values).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                myRef.child(userId).child("appliedTimestamp").setValue(ServerValue.TIMESTAMP);
                                Toast.makeText(getActivity(), "Application sent successfully", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
//                                if (isTaskFinished == true)
//                                getActivity().finish();
                            }
                        }
                    });
                } //endif filepath != null, checkinfobutton
                //filepath: User has selected a file
                else if (isUploadFinished == false && checkInfoButton.isChecked())
                    Toast.makeText(getActivity(), "Please upload your receipt", Toast.LENGTH_SHORT).show();

                else if (isUploadFinished && !checkInfoButton.isChecked())
                    Toast.makeText(getActivity(), "Please agree to the terms", Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(getActivity(), "Please agree to the terms and upload your receipt", Toast.LENGTH_SHORT).show();

            }   //end onClick
        });
    }

    void initButton(View view) {
//        selectButton = view.findViewById(R.id.selectReceipt);
        uploadButton = view.findViewById(R.id.uploadReceipt);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        notification = view.findViewById(R.id.notification);
        notification.setText("Please select a file...");

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                } else
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 15);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filepath != null) //User has selected a file
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
            notification.setText("File selected: " + data.getData().getLastPathSegment());
        } else {
            Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    //Method to acknowledge permission, Override means automatically called  by Android
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 15 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        } else
            Toast.makeText(getContext(), "Please provide permission", Toast.LENGTH_SHORT).show();
    }

    //Upload pdf file
    private void uploadFile(Uri filepath) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        //Store file in storage
//        final String fileName = System.currentTimeMillis() + "_PaymentReceipt";
        final String fileName = userId + "_PaymentReceipt";
        StorageReference mStorageRef = storage.getReference().child("CertApplication_StudentInfo").child("NewApplication").child(userId);
        mStorageRef.child(fileName).putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                DatabaseReference databaseReference = database.getReference().child("CertApplication_StudentInfo").child("NewApplication").child(userId);
                databaseReference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "File submission successful", Toast.LENGTH_SHORT).show();
                            notification.setText("File has been successfully uploaded: " + fileName);
                            progressDialog.dismiss();
                            isUploadFinished = true;
                        } else {
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
                int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setTitle("Apply Certificate");
    }

}
