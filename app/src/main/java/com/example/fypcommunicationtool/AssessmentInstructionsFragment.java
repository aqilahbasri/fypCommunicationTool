package com.example.fypcommunicationtool;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;

public class AssessmentInstructionsFragment extends Fragment implements View.OnClickListener {

    private String reference;
    private CheckBox checkBox;
    private static final String TAG = "InstructionsFragment";

    public AssessmentInstructionsFragment(String reference) {
        this.reference = reference;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assessment_instructions, container, false);

        Button start;
        start = view.findViewById(R.id.start_button);
        checkBox = view.findViewById(R.id.checkBox);

        start.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (checkBox.isChecked()) {
            Intent i = new Intent(getActivity(), AssessmentLevel1Activity.class);
            i.putExtra("docReference", reference);
            Log.e(TAG, "ref: " + reference);
            startActivity(i);
        }

        else
            Toast.makeText(getContext(), "Please agree to the terms", Toast.LENGTH_SHORT).show();
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setTitle("Assessment Instructions");
    }
}