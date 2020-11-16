package com.example.fypcommunicationtool;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AssessmentHomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
            case R.id.startAssessment : i = new Intent(getActivity(), StartAssessmentActivity.class);startActivity(i); break;
            case R.id.viewResults : i = new Intent(getActivity(), ViewResultsActivity.class);startActivity(i); break;
            case R.id.submitCoursework : i = new Intent(getActivity(), SubmitCourseworkActivity.class);startActivity(i); break;
//            case R.id.joinOnlineInterview : i = new Intent(getActivity(), StartAssessment2.class);startActivity(i); break;
            case R.id.applyCertificate : i = new Intent(getActivity(), ApplyCertificateActivity.class);startActivity(i); break;
            default:break;
        }
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setTitle("Assessment");
    }

}