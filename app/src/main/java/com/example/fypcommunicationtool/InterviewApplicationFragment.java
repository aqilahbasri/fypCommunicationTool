package com.example.fypcommunicationtool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class InterviewApplicationFragment extends Fragment {
    public InterviewApplicationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_interview_application_success, container, false);
        Button backToHomeBtn = view.findViewById(R.id.backToHomeBtn);

        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setTitle("Interview Application");
    }

}
