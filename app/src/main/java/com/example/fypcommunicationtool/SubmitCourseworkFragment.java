package com.example.fypcommunicationtool;

import android.Manifest;
import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class SubmitCourseworkFragment extends Fragment {

    private Uri filepath; //Uri = URL for local storage
    ProgressDialog progressDialog;

    FirebaseStorage storage;
    FirebaseDatabase database;

    private String userID;
    private String currentWorkID;

    RecyclerView mRecyclerView;
    private final String TAG = this.getClass().getSimpleName();

    FirebaseRecyclerAdapter adapter;

    private TextView notifTxt;

    public SubmitCourseworkFragment() {
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
        View view = inflater.inflate(R.layout.fragment_submit_coursework, container, false);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRecyclerView = view.findViewById(R.id.list_name);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        DatabaseReference detailsRef = database.getReference().child("ManageCoursework").child("CourseworkQuestions");
        detailsRef.keepSynced(true);

        submitCourseworkAdapter(detailsRef);

        return view;
    }

    public void submitCourseworkAdapter(DatabaseReference detailsRef) {

        FirebaseRecyclerOptions<ManageCourseworkModalClass> options =
                new FirebaseRecyclerOptions.Builder<ManageCourseworkModalClass>()
                        .setQuery(detailsRef, ManageCourseworkModalClass.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<ManageCourseworkModalClass, SubmitCourseworkFragment.MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ManageCourseworkModalClass model) {

//                DatabaseReference ref = database.getReference().child("ManageCoursework").child("CourseworkSubmissions");

                DatabaseReference ref = this.getRef(position);
                Log.i(TAG, "ref: "+ref.getRef());

                ref.child("CourseworkSubmissions").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(userID)) {
                            holder.selectFile.setVisibility(View.GONE);
                            holder.upload.setVisibility(View.GONE);
                            holder.notification.setVisibility(View.VISIBLE);
                            holder.notification.setText("You have submitted your answer for this coursework!");
                        }
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
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            getContext().startActivity(intent);
                        }
                    }
                });

                holder.selectFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            selectPdf();
                        }
                        else
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 15);
                    }
                });

                holder.upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (filepath!=null) //User has selected a file
                            uploadFile(ref, filepath, holder.selectFile, holder.upload);
                        else
                            Toast.makeText(getActivity(), "Please select a file", Toast.LENGTH_SHORT).show();
                    }
                });

                setNotifTextView(holder.notification);
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_submit_coursework, parent, false));
            }
        };

        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView index;
        TextView courseworkName;
        TextView courseworkQuestion;
        TextView dateCreated;
        Button getFile;
        TextView notification;
        Button selectFile;
        Button upload;


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
    private void uploadFile(DatabaseReference reference, Uri filepath, Button selectFile, Button upload) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        //Store file in storage
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

//                        DatabaseReference databaseReference = database.getReference().child("ManageCoursework")
//                                .child("CourseworkSubmissions").child(userID);

                        DatabaseReference databaseReference = reference.child("CourseworkSubmissions").child(userID);
                        databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    selectFile.setVisibility(View.GONE);
                                    upload.setVisibility(View.GONE);

                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "File has been successfully uploaded: "+fileName, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "File submission failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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

        //Method to acknowledge permission, Override means automatically called by Android
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==15 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        }
        else
            Toast.makeText(getContext(), "Please provide permission", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "File has been selected: "+data.getData().getLastPathSegment(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity)getActivity()).setTitle("Submit Coursework");
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void setNotifTextView(TextView notifTxt) {
        this.notifTxt = notifTxt;
    }

    public TextView getNotifTextView() {
        return notifTxt;
    }
}