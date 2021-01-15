package com.example.fypcommunicationtool;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JoinInterviewFragment extends Fragment {

    private RelativeLayout appEmpty, scheduledEmpty;
    private RelativeLayout appFill, scheduledFill;
    private Button applyBtn, joinBtn;
    private final boolean isApplied;
    private final boolean isScheduled;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static final String TAG = "JoinInterviewFragment";

    public JoinInterviewFragment(boolean isApplied, boolean isScheduled) {
        // Required empty public constructor
        Log.e(TAG, "x: " + isApplied + " y: " + isScheduled);
        this.isApplied = isApplied;
        this.isScheduled = isScheduled;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join_interview, container, false);

        appEmpty = view.findViewById(R.id.applicationEmptyLayout);
        scheduledEmpty = view.findViewById(R.id.scheduledLayoutEmpty);
        appFill = view.findViewById(R.id.applicationFillLayout);
        scheduledFill = view.findViewById(R.id.scheduledLayoutFill);
        applyBtn = view.findViewById(R.id.applyButton);
        joinBtn = view.findViewById(R.id.joinButton);

        if (isApplied == false && isScheduled == false) { //not yet apply

            appEmpty.setVisibility(View.VISIBLE);
            scheduledEmpty.setVisibility(View.VISIBLE);

            applyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.CustomMaterialDialog);

                    dialog.setTitle("Confirm application");
                    dialog.setMessage("Once you confirm application, your application will be sent to the system and be reviewed.");
                    dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            sendApplication();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String id = user.getUid();

                            JoinInterviewFragment.AsyncTask task = new JoinInterviewFragment.AsyncTask(id);
                            task.execute();
                        }
                    });

                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

        } else {  //if has applied

            //TODO: get info from db
            appFill.setVisibility(View.VISIBLE);

            if (isScheduled == true) { //if admin has approved
                scheduledFill.setVisibility(View.VISIBLE);

                //TODO: Join video call
                joinBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            } else {  //if admin has not yet approved
                scheduledEmpty.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

    /*
    protected void sendApplication() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        Map values = new HashMap<>();

        Date date = new Date();
        Long dateApplied = date.getTime();

        boolean completeSubmission = false;
        boolean completeAssessment = false;

        DatabaseReference submissionRef = db.getReference().child("ManageCoursework").child("Submissions").child(id);
        DatabaseReference assessmentRef = db.getReference().child("AssessmentMark").child(id);
        if (!submissionRef.equals(null))
            completeSubmission = true;
        if (!assessmentRef.equals(null))
            completeAssessment = true;

        DatabaseReference userRef = db.getReference().child("Users").child(id);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("fullName").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error: "+error.getMessage());
            }
        });

        values.put("dateApplied", dateApplied);
        values.put("sortOrder", 0); //update later in submit coursework fragment
        //TODO: in submit coursework fragment, get score from db
        //TODO: get coursework mark
        values.put("completeSubmission", completeSubmission);
        values.put("completeAssessment", completeAssessment);
        values.put("overallMark", 0); //update value in submit coursework fragment, calculation
        values.put("name", name);

        DatabaseReference applyRef = db.getReference().child("ManageOnlineInterview").child("NewApplication");

        applyRef.child(id).setValue(values).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new InterviewApplicationFragment()).commit();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error: "+e.getMessage());
                Toast.makeText(getContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

     */

    private class AsyncTask extends android.os.AsyncTask {

        private String name, id;
        private boolean completeSubmission = false, completeAssessment = false;

        AsyncTask(String id) {
            this.id = id;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return objects;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            DatabaseReference submissionRef = db.getReference().child("ManageCoursework").child("Submissions").child(id);
            DatabaseReference assessmentRef = db.getReference().child("AssessmentMark").child(id);

            submissionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                        completeSubmission = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            assessmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                        completeAssessment = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            DatabaseReference userRef = db.getReference().child("Users").child(id);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    name = snapshot.child("fullName").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error: " + error.getMessage());
                }
            });
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Map values = new HashMap<>();

            Date date = new Date();
            Long dateApplied = date.getTime();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    values.put("dateApplied", dateApplied);
                    values.put("sortOrder", 0); //update later in submit coursework fragment
                    //TODO: in submit coursework fragment, get score from db
                    //TODO: get coursework mark
                    values.put("completeSubmission", completeSubmission);
                    values.put("completeAssessment", completeAssessment);
                    values.put("overallMark", 0); //update value in submit coursework fragment, calculation
                    values.put("name", name);

                    DatabaseReference applyRef = db.getReference().child("ManageOnlineInterview").child("NewApplication");
                    applyRef.child(id).setValue(values).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                        new InterviewApplicationFragment()).commit();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error: " + e.getMessage());
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 2000);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setTitle("Join Online Interview");
    }
}