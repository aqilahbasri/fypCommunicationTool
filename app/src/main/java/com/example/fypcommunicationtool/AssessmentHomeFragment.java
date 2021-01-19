package com.example.fypcommunicationtool;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AssessmentHomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "AssessmentHomeFragment";

    private boolean isCompleteAssessment;
    private boolean isCompleteSubmission;
    private boolean isCompleteInterview;

    public AssessmentHomeFragment(boolean isCompleteAssessment, boolean isCompleteInterview, boolean isCompleteSubmission) {
        this.isCompleteAssessment = isCompleteAssessment;
        this.isCompleteSubmission = isCompleteSubmission;
        this.isCompleteInterview = isCompleteInterview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_assessment_home, container, false);
        CardView startAssessment = v.findViewById(R.id.startAssessment);
        CardView viewResult = v.findViewById(R.id.viewResults);
        CardView submitCoursework = v.findViewById(R.id.submitCoursework);
        CardView joinOnlineInterview = v.findViewById(R.id.joinOnlineInterview);
        CardView applyCertificate = v.findViewById(R.id.applyCertificate);

        startAssessment.setOnClickListener(this);
        viewResult.setOnClickListener(this);
        submitCoursework.setOnClickListener(this);
        joinOnlineInterview.setOnClickListener(this);
        applyCertificate.setOnClickListener(this);

        if (isCompleteInterview == true && isCompleteSubmission == true && isCompleteAssessment == true) {
            applyCertificate.setCardBackgroundColor(Color.parseColor("#32ACBA"));
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.startAssessment:
                i = new Intent(getActivity(), StartAssessmentActivity.class);
                startActivity(i);
                break;
            case R.id.viewResults:
                i = new Intent(getActivity(), ViewResultsActivity.class);
                startActivity(i);
                break;
            case R.id.submitCoursework:
                i = new Intent(getActivity(), SubmitCourseworkActivity.class);
                startActivity(i);
                break;
            case R.id.joinOnlineInterview:
                i = new Intent(getActivity(), JoinInterviewActivity.class);
                startActivity(i);
                break;
            case R.id.applyCertificate:
                if (isCompleteInterview == true && isCompleteSubmission == true && isCompleteAssessment == true) {
                    i = new Intent(getActivity(), ApplyCertificateActivity.class);
                    startActivity(i);
                } else viewDialog();

                break;
            default:
                break;
        }
    }

    private void viewDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.CustomMaterialDialog);
        dialog.setTitle("Access denied");
        dialog.setMessage("You cannot access this option because you have not completed all assessment sections.");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setTitle("Assessment");
    }

}