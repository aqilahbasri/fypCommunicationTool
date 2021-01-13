package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

public class AssessmentLevel1Activity extends AppCompatActivity {

    private static final String TAG = "AssessmentLevelActivity";

    private WebView questionGIF;
    private EditText answerTxt;
    private TextView sectionTxt, questionTxt, timerTxt;
    FloatingActionButton backBtn, nextBtn;

    ArrayList<AssessmentQuestionsModel> questionList;
    ArrayList<TestSectionModel> sectionList;
    int questionNum;
    int score;
    //    private Toolbar toolbar;
    String data;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_level1);

        Intent intent = getIntent();
        String reference = intent.getStringExtra("docReference");

//        toolbar = (Toolbar) findViewById(R.id.main_learning_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(data.toUpperCase());

        final String questionref = data;

        answerTxt = findViewById(R.id.answer);
        sectionTxt = findViewById(R.id.sectionName);
        questionTxt = findViewById(R.id.question);
        questionGIF = findViewById(R.id.image_question);

        questionList = new ArrayList<>();

        getQuestionList(data);

        getInfoFromDatabase(reference);

        score = 0;
    }

    private void getInfoFromDatabase(String reference) {

        CollectionReference collection = db.document(reference).collection("Sections");

        DocumentReference ref = collection.document();

        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

//                questionList.clear();

                if (task.isSuccessful()) {

                    //TODO: tembak dulu boss, for each dulu yuk, now at section document level
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {

                        if (snapshot.exists()) {

                            sectionList = new ArrayList<>();
                            TestSectionModel model = new TestSectionModel();
                            sectionList.add(model);
                            Log.e(TAG, "size: "+sectionList.size());

                            for (int i = 0; i < sectionList.size(); i++) {
                                Log.e(TAG, snapshot.getId());

                                //TODO: algo for question~~!!!!
//                                snapshot.get

                            }

                            //bila sampai question level
//                            AssessmentQuestionsModel model2 = new AssessmentQuestionsModel();
//                            questionList.add(model2);

                            int sectionSize = Integer.parseInt(snapshot.get("noOfQuestions").toString());

                        } else {
                            Toast.makeText(AssessmentLevel1Activity.this, "Error getting documents",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Toast.makeText(AssessmentLevel1Activity.this, "Error getting documents",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        ArrayList<AssessmentQuestionsModel> questions = new ArrayList<>();

    }

    private void getQuestionList(String data) {
        questionList.clear();
//        reference = FirebaseDatabase.getInstance().getReference().child("Question").child(data);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    QuestionList l = dataSnapshot1.getValue(QuestionList.class);
//                    questionList.add(l);
//                }
//                setQuestion();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        // setQuestion();
    }

    private void setQuestion() {
        if (questionList.size() == 0) {
            Toast.makeText(getApplicationContext(), "Category " + data + " do not have any sign language challenge!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivityLearning.class);
            startActivity(intent);
        } else {
//            questionGIF.loadUrl(questionList.get(0).getQuestion());
            questionGIF.getSettings().setLoadWithOverviewMode(true);
            questionGIF.getSettings().setUseWideViewPort(true);
            questionGIF.setBackgroundColor(0);
//            option1.setText(questionList.get(0).getOption1());
//
//            option2.setText(questionList.get(0).getOption2());
//
//            option3.setText(questionList.get(0).getOption3());
//
//            option4.setText(questionList.get(0).getOption4());

            questionNum = 0;
        }
    }

//        checkAnswer(selectedOption, v);

    /*
    private void checkAnswer(int selectedOption, View view) {
        if (selectedOption == questionList.get(questionNum).getCorrectAnswer()) {
            //Right Answer
            ((Button) view).setBackgroundResource(R.drawable.answerright);
            score++;

        } else {
            //Wrong Answer
            ((Button) view).setBackgroundResource(R.drawable.answerwrong);

//            switch (questionList.get(questionNum).getCorrectAnswer()) {
//                case 1:
//                    option1.setBackgroundResource(R.drawable.answerright);
//                    break;
//                case 2:
//                    option2.setBackgroundResource(R.drawable.answerright);
//                    break;
//                case 3:
//                    option3.setBackgroundResource(R.drawable.answerright);
//                    break;
//                case 4:
//                    option4.setBackgroundResource(R.drawable.answerright);
//                    break;
//
//            }
        }

        changeQuestion();
    }*/

    private void changeQuestion() {
        if (questionNum < questionList.size() - 1) {
            questionNum++;

            playAnim(questionGIF, 0, 0);
//            playAnim(option1, 0, 1);
//            playAnim(option2, 0, 2);
//            playAnim(option3, 0, 3);
//            playAnim(option4, 0, 4);

        } else {
            //display score
//            int finalscore = score*10;
            double numberQ = questionList.size();
            double percentscore = score * 100 / numberQ;
            String finalscore = score + "/" + questionList.size();
            Intent intent = new Intent(AssessmentLevel1Activity.this, Score.class);
            intent.putExtra("finalscore", finalscore);
            intent.putExtra("score", Double.toString(percentscore));
            startActivity(intent);
        }
    }

    private void playAnim(final View view, final int value, final int viewNum) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (value == 0) {
                            switch (viewNum) {
//                                case 0:
//                                    questionGIF.loadUrl(questionList.get(questionNum).getQuestion());
//                                    questionGIF.getSettings().setLoadWithOverviewMode(true);
//                                    questionGIF.getSettings().setUseWideViewPort(true);
//                                    questionGIF.setBackgroundColor(0);
//                                    break;
//                                case 1:
//                                    ((Button) view).setText(questionList.get(questionNum).getOption1());
//                                    ((Button) view).setBackgroundResource(R.drawable.answerbtn);
//                                    break;
//                                case 2:
//                                    ((Button) view).setText(questionList.get(questionNum).getOption2());
//                                    break;
//                                case 3:
//                                    ((Button) view).setText(questionList.get(questionNum).getOption3());
//                                    break;
//                                case 4:
//                                    ((Button) view).setText(questionList.get(questionNum).getOption4());
//                                    break;

                            }


                            if (viewNum != 0)
                                ((Button) view).setBackgroundResource(R.drawable.answerbtn);

                            playAnim(view, 1, viewNum);

                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        final Dialog exitchallDialog = new Dialog(this);
        exitchallDialog.setContentView(R.layout.exitchallenge);
        Button yes = (Button) exitchallDialog.findViewById(R.id.exitchall);
        Button no = (Button) exitchallDialog.findViewById(R.id.contchall);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivityLearning.class);
                startActivity(intent);

                Intent intent1 = new Intent(getApplicationContext(), ChallengeBGM.class);
                stopService(intent1);

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitchallDialog.dismiss();
            }
        });

        exitchallDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        exitchallDialog.show();

    }

}
