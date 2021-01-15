package com.example.fypcommunicationtool;

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

    private boolean status = false;
    private static final String TAG = "AssessmentHomeFragment";

    public AssessmentHomeFragment() {
        // Required empty public constructor
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("AssessmentMark");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id).exists()) {
                    applyCertificate.setCardBackgroundColor(Color.parseColor("#32ACBA"));
                } //TODO: dia tak boleh apply certificate
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "error: " + error.getMessage());
            }
        });

        startAssessment.setOnClickListener(this);
        viewResult.setOnClickListener(this);
        submitCoursework.setOnClickListener(this);
        joinOnlineInterview.setOnClickListener(this);
        applyCertificate.setOnClickListener(this);

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
                i = new Intent(getActivity(), ApplyCertificateActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setTitle("Assessment");
    }

}