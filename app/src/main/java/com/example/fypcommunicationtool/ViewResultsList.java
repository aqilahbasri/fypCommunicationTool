package com.example.fypcommunicationtool;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

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

        DatabaseReference detailsRef = database.getReference().child("AssessmentMark").child("Level1").child("Endun Yvonne");
        detailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mark1, mark2, mark3, overallMark;
                mark1 = snapshot.child("assessmentMark").getValue().toString();
                mark2 = snapshot.child("courseworkMark").getValue().toString();
                mark3 = snapshot.child("interviewMark").getValue().toString();
                overallMark = snapshot.child("overallMark").getValue().toString();

                markText1.setText(mark1);
                markText2.setText(mark2);
                markText3.setText(mark3);
                overallMarkText.setText(overallMark);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });

        passingGradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ViewResultsActivity) activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PassingGradeFragment()).addToBackStack(TAG).commit();
            }
        });

    }
}