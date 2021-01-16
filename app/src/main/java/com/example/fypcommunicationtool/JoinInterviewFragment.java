package com.example.fypcommunicationtool;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypcommunicationtool.assessment.CallingActivity;
import com.example.fypcommunicationtool.assessment.OutgoingInterviewActivity;
import com.example.fypcommunicationtool.assessment.Users;
import com.example.fypcommunicationtool.assessment.UsersListener;
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

public class JoinInterviewFragment extends Fragment implements UsersListener {

    private RelativeLayout appEmpty, scheduledEmpty;
    private RelativeLayout appFill, scheduledFill;
    private Button applyBtn, joinBtn;
    private TextView dateApplied, timeApplied, statusTxt;
    private TextView dateTxt, timeTxt, interviewerTxt;
    private boolean isApplied;
    private boolean isScheduled;
    private UsersListener usersListener;
    private Users users;

    private String userId, interviewerId;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static final String TAG = "JoinInterviewFragment";

    private String calledBy ="";

    public JoinInterviewFragment(boolean isApplied, boolean isScheduled, Users users) {
        // Required empty public constructor
        this.isApplied = isApplied;
        this.isScheduled = isScheduled;
        this.users = users;
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
        dateTxt = view.findViewById(R.id.dateTxt);
        timeTxt = view.findViewById(R.id.timeTxt);
        interviewerTxt = view.findViewById(R.id.interviewerTxt);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

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

        } else {  //if has applied

            //TODO: kalau dah schedule nak buat apa
            appFill.setVisibility(View.VISIBLE);
            updateUI(userId);

            if (isScheduled == true) { //if admin has approved
                scheduledFill.setVisibility(View.VISIBLE);
                updateScheduleUI(userId);

                //TODO: Join video call
                joinBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initiateVideoMeeting(users);
                            }}, 1500);
                    }
                });

            } else {  //if admin has not yet approved
                scheduledEmpty.setVisibility(View.VISIBLE);
            }
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

                    statusTxt.setText("Awaiting approval");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    @Override
    public void initiateVideoMeeting(Users users) {
        if (users.getFcmToken() == null || users.getFcmToken().trim().isEmpty()) {
            Toast.makeText(getContext(), users.getFullName()+" is not available for meeting", Toast.LENGTH_SHORT)
                    .show();
        }
        else {

            //TODO: admin side, set id siapa yang approve
            Intent intent = new Intent(getContext(), CallingActivity.class);
            intent.putExtra("visit_user_id", interviewerId);
            startActivity(intent);

//            Intent intent = new Intent(getContext(), OutgoingInterviewActivity.class);
//            intent.putExtra("users", users);
//            intent.putExtra("type", "video");
            startActivity(intent);
//            Toast.makeText(getContext(), "Video meeting with "+users.getFullName(), Toast.LENGTH_SHORT)
//                    .show();
        }
    }

    @Override
    public void initiateAudioMeeting(Users users) {

    }

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

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        checkForReceivingCall() ;
//
//    }

    private void checkForReceivingCall() {

        DatabaseReference ref = db.getReference().child("Users").child(userId).child("Ringing");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("Ringing")) {
                    calledBy = snapshot.child("Ringing").getValue().toString();

                    Intent intent = new Intent(getContext(), CallingActivity.class);
                    intent.putExtra("visit_user_id", calledBy);
                    startActivity(intent);
                    getActivity().finish();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setTitle("Join Online Interview");
    }
}