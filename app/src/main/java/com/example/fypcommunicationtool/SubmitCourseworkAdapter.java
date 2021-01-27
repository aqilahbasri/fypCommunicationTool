package com.example.fypcommunicationtool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class SubmitCourseworkAdapter extends FirebaseRecyclerAdapter<ManageCourseworkModalClass, SubmitCourseworkAdapter.MyViewHolder> {

    private Activity activity;

    Uri filepath; //Uri = URL for local storage
    ProgressDialog progressDialog;

    FirebaseStorage storage;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private String userID;

    Button selectFile;
    Button upload;
    TextView notification;

    private final String TAG = this.getClass().getSimpleName();

    SubmitCourseworkAdapter(FirebaseRecyclerOptions<ManageCourseworkModalClass> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_layout_submit_coursework, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ManageCourseworkModalClass model) {

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = database.getReference().child("ManageCoursework").child("CourseworkSubmissions");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userID)) {
                    selectFile.setVisibility(View.GONE);
                    upload.setVisibility(View.GONE);
                    notification.setVisibility(View.VISIBLE);
                    notification.setText("You have submitted your answer for this coursework!");
                }
                else
                    notification.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.index.setText(String.valueOf(position+1));
        holder.courseworkName.setText(model.getCourseworkName());
        holder.courseworkQuestion.setText(model.getCourseworkQuestion());
        holder.dateCreated.setText(model.getDateCreated());

        holder.getFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getCourseworkFile()));
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(intent);
                }
            }
        });

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                }
                else
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 15);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filepath!=null) //User has selected a file
                    uploadFile(filepath);
                else
                    Toast.makeText(activity, "Please select a file", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView index;
        TextView courseworkName;
        TextView courseworkQuestion;
        TextView dateCreated;
        Button getFile;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            index = itemView.findViewById(R.id.textView1);
            courseworkName = itemView.findViewById(R.id.textView2);
            courseworkQuestion = itemView.findViewById(R.id.textView3);
            dateCreated = itemView.findViewById(R.id.textView4);
            getFile = itemView.findViewById(R.id.review_button);

            upload = itemView.findViewById(R.id.uploadButton);
            selectFile = itemView.findViewById(R.id.selectButton);
            notification = itemView.findViewById(R.id.notification);

        }
    }

    //Upload pdf file
    private void uploadFile(Uri filepath) {

        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        //Store file in storage
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final String fileName = userID+"_Coursework";
        StorageReference mStorageRef = storage.getReference().child("ManageCoursework").child("CourseworkSubmissions").child(userID);
        mStorageRef.child(fileName).putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mStorageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Calendar calendar = Calendar.getInstance();

                        Map<String, Object> map = new HashMap<>();
                        map.put("courseworkFile", uri.toString());
                        map.put("submittedDate", calendar.getTimeInMillis());
                        map.put("applicantId", userID);

                        DatabaseReference databaseReference = database.getReference().child("ManageCoursework")
                                .child("CourseworkSubmissions").child(userID);
                        databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    selectFile.setVisibility(View.GONE);
                                    upload.setVisibility(View.GONE);
                                    notification.setVisibility(View.VISIBLE);

                                    progressDialog.dismiss();
                                    Toast.makeText(activity, "File submission successful", Toast.LENGTH_SHORT).show();
                                    notification.setText("File has been successfully uploaded: "+fileName);
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(activity, "File submission failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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

    //Method to acknowledge permission, Override means automatically called by Android
//    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==15 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        }
        else
            Toast.makeText(activity, "Please provide permission", Toast.LENGTH_SHORT).show();
    }

    private void selectPdf() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        activity.startActivityForResult(intent, 20);
    }

    //To check if user successfully select file
//    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 20 && resultCode == RESULT_OK && data != null) {
            filepath = data.getData(); //return uri of selected file
            notification.setVisibility(View.VISIBLE);
            notification.setText("File has been selected: "+data.getData().getLastPathSegment());
        } else {
            Toast.makeText(activity, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    public void setActivity (Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }
}