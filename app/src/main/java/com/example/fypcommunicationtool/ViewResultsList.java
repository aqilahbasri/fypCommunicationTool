package com.example.fypcommunicationtool;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewResultsList {

    private static final String TAG = "ViewResultsList";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Activity activity;
    View view;
    TextView studentNameText;
    TextView markText1, markText2, markText3, overallMarkText;
    TextView sectionText1, sectionText2, sectionText3, sectionText4;
    Button downloadBtn, passingGradeBtn;

    private int assessmentMark = 0;
    private int courseworkMark = 0;
    private int interviewMark = 0;

    protected ViewResultsList(Activity activity, View view) {
        super();
        this.activity = activity;
        this.view = view;
    }
    protected void displayList () {

        sectionText1 = view.findViewById(R.id.section_name1);
        sectionText2 = view.findViewById(R.id.section_name2);
        sectionText3 = view.findViewById(R.id.section_name3);
        sectionText4 = view.findViewById(R.id.section_name4);

        markText1 = view.findViewById(R.id.mark_1);
        markText2 = view.findViewById(R.id.mark_2);
        markText3 = view.findViewById(R.id.mark_3);
        overallMarkText = view.findViewById(R.id.mark_4);

        sectionText1.setText("Assessment");
        sectionText2.setText("Coursework");
        sectionText3.setText("Interview");
        sectionText4.setText("Overall Mark");

        studentNameText = view.findViewById(R.id.student_name);
        studentNameText.setText("Your results: ");
        passingGradeBtn = view.findViewById(R.id.passing_grade_btn);

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference assessmentRef = database.getReference().child("AssessmentMark");
        DatabaseReference courseworkRef = database.getReference().child("ManageCoursework").child("CourseworkSubmissions");
        DatabaseReference interviewRef = database.getReference().child("ManageOnlineInterview").child("CompletedInterview");
        assessmentRef.keepSynced(true);
        courseworkRef.keepSynced(true);
        interviewRef.keepSynced(true);

        assessmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id).exists()) {
                    String mark = snapshot.child(id).child("Score").getValue().toString();
                    assessmentMark = Integer.parseInt(mark);
                    markText1.setText(mark);
                } else {
                    markText1.setText("No record");
                    markText1.setTextColor(Color.parseColor("#FF0000"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "error: " + error.getMessage());
            }
        });

        courseworkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id).exists() && snapshot.child(id).child("courseworkMark").exists()) {
                    String mark = snapshot.child(id).child("courseworkMark").getValue().toString();
                    courseworkMark = Integer.parseInt(mark);
                    markText2.setText(mark);
                } else  {
                    markText2.setText("No record");
                    markText2.setTextColor(Color.parseColor("#FF0000"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "error: " + error.getMessage());
            }
        });

        interviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id).exists() && snapshot.child(id).child("interviewMark").exists()) {
                    String mark = snapshot.child(id).child("interviewMark").getValue().toString();
                    interviewMark = Integer.parseInt(mark);
                    markText3.setText(mark);
                }
                else {
                    markText3.setTextColor(Color.parseColor("#FF0000"));
                    markText3.setText("No record");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "error: " + error.getMessage());
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (assessmentMark != 0 && courseworkMark != 0 && interviewMark!=0) {
                    int overallMark = assessmentMark + courseworkMark + interviewMark;
                    overallMarkText.setText(String.valueOf(overallMark));
                }
                else {
                    overallMarkText.setText("Incomplete");
                    overallMarkText.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        }, 1500);

        passingGradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ViewResultsActivity) activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PassingGradeFragment()).addToBackStack(TAG).commit();
            }
        });

    }
}