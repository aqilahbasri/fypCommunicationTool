package com.example.fypcommunicationtool;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AssessmentInstructionsFragment extends Fragment implements View.OnClickListener {

    private String reference;
    private CheckBox checkBox;
    private static final String TAG = "InstructionsFragment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Long duration;

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
        DocumentReference ref = db.document(reference);

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                duration = snapshot.getLong("duration");
                Log.e(TAG, snapshot.getReference().getPath());
                Log.e(TAG, snapshot.getString("levelName"));
                Log.e(TAG, snapshot.getLong("duration").toString());

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

        if (checkBox.isChecked()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String id = user.getUid();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference().child("AssessmentMark").child(id).child("Assessment");
            ref.child("Status").setValue(true);

            Intent i = new Intent(getActivity(), AssessmentLevel1Activity.class);
            i.putExtra("docReference", reference);
            i.putExtra("duration", duration);
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