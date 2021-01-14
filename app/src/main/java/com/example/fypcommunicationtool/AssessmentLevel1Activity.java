package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AssessmentLevel1Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AssessmentLevelActivity";

    private WebView questionGIF;
    private EditText answerTxt;
    private TextView sectionTxt, questionTxt, timerTxt;
    ImageButton backBtn, nextBtn;
    private CardView card;

    ArrayList<AssessmentQuestionsModel> questionList;
    ArrayList<TestSectionModel> sectionList;
    int questionNum, sectionNum;
    int score;
    Long duration;

    private Toolbar toolbar;
    ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_level1);

        Intent intent = getIntent();
        String reference = intent.getStringExtra("docReference");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assessment");

        answerTxt = findViewById(R.id.answer);
        sectionTxt = findViewById(R.id.sectionName);
        questionTxt = findViewById(R.id.question);
        card = findViewById(R.id.cardview_id);

        questionGIF = findViewById(R.id.image_question);
        questionGIF.getSettings().setLoadWithOverviewMode(true);
        questionGIF.getSettings().setUseWideViewPort(true);

        nextBtn = findViewById(R.id.nextBtn);
        backBtn = findViewById(R.id.backBtn);
        timerTxt = findViewById(R.id.timerTxt);
        progressBar = findViewById(R.id.determinateBar);

        sectionList = new ArrayList<>();
        questionList = new ArrayList<>();

        //do for back button as well
        nextBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        questionNum = 0;
        sectionNum = 0;
        score = 0;

        Thread runFirst = new Thread(new Runnable() {
            @Override
            public void run() {
                DocumentReference ref = db.document(reference);
                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snapshot = task.getResult();
                        Log.e(TAG, snapshot.getReference().getPath());
                        Log.e(TAG, snapshot.getString("levelName"));
                        Log.e(TAG, snapshot.getLong("duration").toString());
                        duration = snapshot.getLong("duration");
                        setDuration(snapshot.getLong("duration"));

                    }
                });
            }
        });

        runFirst.start();
        try {
            runFirst.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "time: "+getDuration());
//                timerTxt.setText(duration.toString());
                getAssessmentQuestionsModel(reference);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        new CountDownTimer(getDuration(), 1000) {
            public void onTick(long millisUntilFinished) {
//                mTextViewCountDown.setText("Time left: " + millisUntilFinished / 1000);
                String text = String.format(Locale.getDefault(), "Time left: %02d min: %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                timerTxt.setText(text);

                getAssessmentQuestionsModel(reference);
            }

            public void onFinish() {
                timerTxt.setText("Your time is up!");
            }
        }.start();
        */

    }

    private void getAssessmentQuestionsModel(String reference) {

        //step 1: ambil path for section
        CollectionReference sectionRef = db.document(reference).collection("Sections");
        Query query = sectionRef.orderBy("sectionName");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    questionList.clear();
                    for (DocumentSnapshot secSnapshot : task.getResult()) {
                        TestSectionModel testModel = secSnapshot.toObject(TestSectionModel.class);
                        sectionList.add(testModel);
                        sectionTxt.setText(testModel.getSectionName());

                        //Question level
                        CollectionReference testRef = sectionRef.document(secSnapshot.getId()).collection("Questions");
                        testRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {

                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot testSnapshot : task1.getResult()) {
                                        AssessmentQuestionsModel model = testSnapshot.toObject(AssessmentQuestionsModel.class);
                                        questionList.add(model);
                                        sectionTxt.setText(secSnapshot.getString("sectionName"));
                                    }
                                    setQuestion();
                                }
                            }
                        }); //end question
                    }
                }
            }
        });
    }

    private void setQuestion() {
        if (questionList.size() == 0) {
//            Toast.makeText(getApplicationContext(), "Category " + data + " do not have any sign language challenge!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AssessmentMenuActivity.class);
            startActivity(intent);
        } else {
            questionTxt.setText(questionList.get(0).getQuestionDetail());
            questionGIF.loadUrl(questionList.get(0).getGifUrl());
        }
    }

    @Override
    public void onClick(View v) {

        //TODO: Save terus answer dalam database, compare with editText.getText()
        String answer = answerTxt.getText().toString();
        checkAnswer(answer, v);
    }

    private void checkAnswer(String answer, View view) {
        if (answer.equalsIgnoreCase(questionList.get(questionNum).getCorrectAnswer())) {
            //Right Answer
//            ((Button) view).setBackgroundResource(R.drawable.answerright);
            score++;

        }
        changeQuestion();
    }

    private void changeQuestion() {

        if (questionNum < questionList.size() - 1) {
            questionNum++;
            playAnim(0, 0);
        } else {
            //display score
            double numberQ = questionList.size();
            double percentscore = score * 100 / numberQ;
            String finalscore = score + "/" + questionList.size();
            Intent intent = new Intent(AssessmentLevel1Activity.this, Score.class);
            intent.putExtra("finalscore", finalscore);
            intent.putExtra("score", Double.toString(percentscore));
            startActivity(intent);
            //Questions.this.finish();
        }
    }

    private void playAnim(final int value, final int viewNum) {

        card.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        questionGIF.loadUrl(questionList.get(questionNum).getGifUrl());
                        questionGIF.refreshDrawableState();
                        questionTxt.setText(questionList.get(questionNum).getQuestionDetail());

                        playAnim(1, viewNum);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
    }


    protected Long getDuration() {
        return duration;
    }

    protected void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public void onBackPressed() {
        final Dialog exitchallDialog = new Dialog(this);
        exitchallDialog.setContentView(R.layout.exitchallenge);
        Button yes = (Button) exitchallDialog.findViewById(R.id.exitchall);
        Button no = (Button) exitchallDialog.findViewById(R.id.contchall);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AssessmentMenuActivity.class);
                startActivity(intent);
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
