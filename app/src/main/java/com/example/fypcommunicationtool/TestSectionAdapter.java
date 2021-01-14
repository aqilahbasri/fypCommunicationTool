package com.example.fypcommunicationtool;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.firestore.DocumentReference;

public class TestSectionAdapter extends FirestoreRecyclerAdapter<TestSectionModel, TestSectionAdapter.MyViewHolder> {

    Activity activity;
    private static final String TAG = "TestSectionAdapter";

    public TestSectionAdapter(@NonNull FirestoreRecyclerOptions<TestSectionModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TODO: tukar adapter
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_assessment_questions, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, @NonNull final TestSectionModel model) {

        String pos = String.valueOf(position+1);
        holder.positionTxt.setText(pos);
        holder.sectionNameTxt.setText(model.getSectionName());
        holder.sectionMarkTxt.setText(String.valueOf(model.getSectionPassMark()));
        holder.noOfQuestionsTxt.setText(String.valueOf(model.getNoOfQuestions()));
        final DocumentReference reference = getSnapshots().getSnapshot(position).getReference();

//        holder.sectionNameTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((ManageTestSettingsActivity) getActivity()).getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, new ViewTestLevelFragment(getSnapshots().getSnapshot(position)
//                                .getId())).addToBackStack(null).commit();
//            }
//        });

    }

    protected void setActivity(Activity activity) {
        this.activity = activity;
    }

    protected Activity getActivity() {
        return activity;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView positionTxt;
        TextView sectionNameTxt;
        TextView sectionMarkTxt;
        TextView noOfQuestionsTxt;
        ImageButton editButton, deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            positionTxt = itemView.findViewById(R.id.textView1);
            sectionNameTxt = itemView.findViewById(R.id.textView2);
            sectionMarkTxt = itemView.findViewById(R.id.textView3);
            noOfQuestionsTxt = itemView.findViewById(R.id.textView4);
        }
    }
}

