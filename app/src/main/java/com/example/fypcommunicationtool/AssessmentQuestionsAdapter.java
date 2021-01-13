package com.example.fypcommunicationtool;

import android.annotation.SuppressLint;
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

public class AssessmentQuestionsAdapter extends FirestoreRecyclerAdapter<AssessmentQuestionsModel, AssessmentQuestionsAdapter.MyViewHolder> {

    Activity activity;
    private static final String TAG = "AssessmentQuestionsAdapter";

    public AssessmentQuestionsAdapter(@NonNull FirestoreRecyclerOptions<AssessmentQuestionsModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_assessment_questions, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull final AssessmentQuestionsModel model) {
        holder.pos.setText(String.valueOf(position+1));
        holder.questionID.setText(model.getQuestionID());

        final DocumentReference reference = getSnapshots().getSnapshot(position).getReference();

        holder.questionID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ViewQuestionDetailDialog viewQuestionDetailDialog = new ViewQuestionDetailDialog(reference, position);
//                viewQuestionDetailDialog.show(((ManageQuestionsActivity) getActivity())
//                        .getSupportFragmentManager(), "EditQuestionsDialog");
            }
        });

        holder.questionType.setText(model.getQuestionType());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EditLevel1QuestionFragment fragment = EditLevel1QuestionFragment.getInstance();
//                ((ManageQuestionsActivity) getActivity()).getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
//                fragment.setPosition(position);
//                fragment.setReference(reference);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DeleteLevel1QuestionDialog deleteLevel1QuestionDialog = new DeleteLevel1QuestionDialog(reference, position);
//                deleteLevel1QuestionDialog.show(((ManageQuestionsActivity) getActivity())
//                        .getSupportFragmentManager(), TAG);
            }
        });
    }

    protected void setActivity(Activity activity) {
        this.activity = activity;
    }

    protected Activity getActivity() {
        return activity;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pos;
        TextView questionID;
        TextView questionType;
        ImageButton editButton;
        ImageButton deleteButton;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pos = itemView.findViewById(R.id.textView1);
            questionID = itemView.findViewById(R.id.textView2);
            questionType = itemView.findViewById(R.id.textView3);
//            editButton = itemView.findViewById(R.id.edit_button);
//            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}

class AssessmentQuestionsModel {

    Long dateAdded;
    Long dateModified;
    String questionID;
    String questionDetail;
    String questionType;
    String correctAnswer;
    String gifUrl;
    String engCaption, malayCaption;


    //Do for gif also


    public AssessmentQuestionsModel() {
    }

    public AssessmentQuestionsModel(Long dateAdded, Long dateModified, String questionID, String questionDetail,
                                    String questionType, String correctAnswer, String gifUrl, String engCaption,
                                    String malayCaption) {
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.questionID = questionID;
        this.questionDetail = questionDetail;
        this.questionType = questionType;
        this.correctAnswer = correctAnswer;
        this.gifUrl = gifUrl;
        this.engCaption = engCaption;
        this.malayCaption = malayCaption;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Long getDateModified() {
        return dateModified;
    }

    public void setDateModified(Long dateModified) {
        this.dateModified = dateModified;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestionDetail() {
        return questionDetail;
    }

    public void setQuestionDetail(String questionDetail) {
        this.questionDetail = questionDetail;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getEngCaption() {
        return engCaption;
    }

    public void setEngCaption(String engCaption) {
        this.engCaption = engCaption;
    }

    public String getMalayCaption() {
        return malayCaption;
    }

    public void setMalayCaption(String malayCaption) {
        this.malayCaption = malayCaption;
    }
}
