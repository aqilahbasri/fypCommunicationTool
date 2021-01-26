package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
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
    private TextView sectionTxt, questionTxt, timerTxt, questionCounter;
    ImageButton backBtn, nextBtn;
    private CardView card;

    ArrayList<AssessmentQuestionsModel> questionList;
    ArrayList<TestSectionModel> sectionList;
    int questionNum, sectionNum;
    int score;

    private Toolbar toolbar;
    ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_level1);

        Intent intent = getIntent();
        String reference = intent.getStringExtra("docReference");
        Long duration = intent.getLongExtra("duration", 0);
        Log.i(TAG, "duration: "+duration);

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
//        questionGIF.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        nextBtn = findViewById(R.id.nextBtn);
        backBtn = findViewById(R.id.backBtn);   //TODO: OnClick back button
        timerTxt = findViewById(R.id.timerTxt);
        questionCounter = findViewById(R.id.questionLeftTxt);
        progressBar = findViewById(R.id.determinateBar);

        sectionList = new ArrayList<>();
        questionList = new ArrayList<>();

        //do for back button as well
        nextBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        questionNum = 0;
        sectionNum = 0;
        score = 0;

        new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), "Time left: %02d hr %02d min: %02d sec",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)%60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                timerTxt.setText(text);
            }

            //TODO: code bila dah habis
            public void onFinish() {
                timerTxt.setText("Your time is up!");
            }
        }.start();

        getAssessmentQuestionsModel(reference);
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
                        Query query1 = testRef.limit(6);
                        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
        questionCounter.setText((questionNum + 1) + " / " + 6);
        progressBar.setMax(questionList.size());
        progressBar.setProgress(questionNum + 1);

        if (questionList.size() == 0) {
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
        checkAnswer(answer);
    }

    private void checkAnswer(String answer) {
        if (answer.equalsIgnoreCase(questionList.get(questionNum).getCorrectAnswer())) {
            score++;
        }
        changeQuestion();
    }

    private void changeQuestion() {

        if (questionNum < questionList.size() - 2) {
            questionNum++;
//            questionGIF.loadUrl("about:blank");
            playAnim(0);
        } else {

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Confirm submission");
            dialog.setMessage("Are you sure you want to submit?");
            dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    submitAnswer();
                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            dialog.show();
        }
    }

    private void playAnim(final int value) {

        card.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
//                        questionGIF.clearCache(true);[p

//                        questionGIF.reload();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

//                        questionGIF.postDelayed(new Runnable() {
//
//                            @Override
//                            public void run() {
                        questionGIF.clearCache(true);
                                questionGIF.loadUrl(questionList.get(questionNum).getGifUrl());
//                            }
//                        }, 500);

//                        questionGIF.loadUrl(questionList.get(questionNum).getGifUrl());
                        questionGIF.refreshDrawableState();

//                        questionGIF.
                        questionTxt.setText(questionList.get(questionNum).getQuestionDetail());

                        questionCounter.setText((questionNum + 1) + " / " + 6);
                        progressBar.setProgress(questionNum + 1);

                        playAnim(1);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
    }

    protected void submitAnswer() {
        //TODO: set mark for each question
        double numberQ = 6;
        double percentScore = score * 100 / numberQ;
        String finalScore = score + "/" + 6;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("AssessmentMark").child(id);
        reference.child("Score").setValue(percentScore);

        Intent intent = new Intent(AssessmentLevel1Activity.this, AssessmentLevelFinish.class);
        intent.putExtra("finalScore", finalScore);
        intent.putExtra("score", percentScore);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Are you sure you want to exit?");
        dialog.setMessage("Once you close this window, you need to contact the admins if you wish to take this test.");
        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), AssessmentMenuActivity.class);
                startActivity(intent);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

}