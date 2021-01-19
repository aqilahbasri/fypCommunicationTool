package com.example.fypcommunicationtool;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypcommunicationtool.assessment.CallingActivity;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class JoinInterviewFragment extends Fragment {

    private RelativeLayout appEmpty, scheduledEmpty;
    private RelativeLayout appFill, scheduledFill;
    private Button applyBtn, joinBtn;
    private TextView dateApplied, timeApplied, statusTxt, applicationTxt;
    private TextView dateTxt, timeTxt, interviewerTxt;
    private boolean isApplied, isScheduled, isCompleted;

    private String userId, interviewerId;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static final String TAG = "JoinInterviewFragment";

//    private String calledBy = "";

    public JoinInterviewFragment(boolean isApplied, boolean isScheduled, boolean isCompleted) {
        this.isApplied = isApplied;
        this.isScheduled = isScheduled;
        this.isCompleted = isCompleted;
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
        dateApplied = view.findViewById(R.id.dateApplied);
        timeApplied = view.findViewById(R.id.timeApplied);
        statusTxt = view.findViewById(R.id.statusTxt);
        applicationTxt = view.findViewById(R.id.applicationTxt);
        dateTxt = view.findViewById(R.id.dateTxt);
        timeTxt = view.findViewById(R.id.timeTxt);
        interviewerTxt = view.findViewById(R.id.interviewerTxt);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        if (isApplied == false && isScheduled == false && isCompleted == false) { //not yet apply

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
                            JoinInterviewFragment.AsyncTask task = new JoinInterviewFragment.AsyncTask(userId);
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

        } else if (isApplied == true && isScheduled == false) { //if has applied but not approved by admin

            appFill.setVisibility(View.VISIBLE);
            scheduledEmpty.setVisibility(View.VISIBLE);
            statusTxt.setText("Awaiting approval");
            updateUI(userId);

        } else if (isScheduled == true && isCompleted == false) {   //if has scheduled (already approved)

            appEmpty.setVisibility(View.VISIBLE);
            applicationTxt.setText("Your interview request has been approved. Your schedule is shown below.");
            applyBtn.setVisibility(View.GONE);

            scheduledFill.setVisibility(View.VISIBLE);
            updateScheduleUI(userId);

            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initiateVideoMeeting();
                        }
                    }, 1500);
                }
            });

        } else if (isCompleted == true) { //handle if has completed
            appEmpty.setVisibility(View.VISIBLE);
            applyBtn.setVisibility(View.GONE);
            scheduledEmpty.setVisibility(View.VISIBLE);
            applicationTxt.setText("Your interview has been completed. Your mark can be viewed in the \"View Marks\" section. ");
        }
        return view;
    }

    protected void updateUI(String id) {
        DatabaseReference reference = db.getReference().child("ManageOnlineInterview").
                child("NewApplication").child(id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long time = Long.parseLong(snapshot.child("dateApplied").getValue().toString());
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                    sdf1.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                    String dateStr = sdf1.format(time);

                    SimpleDateFormat sdf2 = new SimpleDateFormat("hh.mm aa");
                    sdf2.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                    String timeStr = sdf2.format(time);

                    dateApplied.setText(dateStr);
                    timeApplied.setText(timeStr);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }

    protected void updateScheduleUI(String id) {
        DatabaseReference reference = db.getReference().child("ManageOnlineInterview").
                child("ScheduledInterview").child(id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long time = Long.parseLong(snapshot.child("interviewTime").getValue().toString());
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                    sdf1.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                    String dateStr = sdf1.format(time);

                    SimpleDateFormat sdf2 = new SimpleDateFormat("hh.mm aa");
                    sdf2.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                    String timeStr = sdf2.format(time);

                    dateTxt.setText(dateStr);
                    timeTxt.setText(timeStr);
                    interviewerTxt.setText(snapshot.child("interviewerName").getValue().toString());

                    interviewerId = snapshot.child("interviewerId").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void initiateVideoMeeting() {
        Intent intent = new Intent((JoinInterviewActivity) getActivity(), CallingActivity.class);
        intent.putExtra("visit_user_id", interviewerId);
        startActivity(intent);
    }

    private class AsyncTask extends android.os.AsyncTask {

        private String name, id;
        private boolean completeSubmission = false, completeAssessment = false;
        private int mark;

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
                    values.put("userId", userId);
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