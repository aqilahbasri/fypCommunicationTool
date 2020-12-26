package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class Score extends AppCompatActivity implements View.OnClickListener{

    private Button backhome;
    private TextView score;

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference, userdetailRef;
    private long xp;
    private String userID;

    long uploadXP;

    String username;
    String profileimage;

    Dialog encDialog;
    TextView rankTitle, xptext;
    ImageView chessRank, closebtn;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("LEADERBOARD");
        userdetailRef = FirebaseDatabase.getInstance().getReference("Users");

        ChallengeBGM(view);

        backhome = (Button) findViewById(R.id.backhome);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backhome();
            }
        });

        score = findViewById(R.id.score);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String data = extras.getString("finalscore"); //correct/numberQ
        String dataa = extras.getString("score"); //percent
        score.setText(data);

        double mark = Double.parseDouble(dataa);
        int markint = (int) Math.round(mark); //percentage mark to determine encourage word

        xp = (int) Math.round(mark) * 200; //to add xp


        //retrieve username n profilepic
        userdetailRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("userID").getValue().toString();
                profileimage = dataSnapshot.child("profileImage").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(userID)) {
                        // run some code
                        String getXP = dataSnapshot.child(userID).child("xp").getValue().toString();
                        long ixp = Integer.parseInt(getXP);
                        uploadXP = ixp + xp;
                        databaseReference.child(userID).child("xp").setValue(uploadXP);

                    }else {
                        UploadScore userscore = new UploadScore(xp, username, profileimage);
                        databaseReference.child(userID).setValue(userscore);
                        uploadXP = 0;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        encDialog = new Dialog(this);



        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (markint<=39) {
                    encLow(xp, uploadXP);

                }else if(markint>=40 && markint<=79){
                    encMedium(xp, uploadXP);

                }else{
                    encHigh(xp, uploadXP);
                }

            }
        }, 2000);


    }

    public void backhome() {
        Intent intent = new Intent(this, MainActivityLearning.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

    //mark>=80
    public void encHigh(long xp, long uploadXP) {
        encDialog = new Dialog(this);
        encDialog.setContentView(R.layout.enc_word_high);
        closebtn = (ImageView) encDialog.findViewById(R.id.closebtn);
        rankTitle = (TextView) encDialog.findViewById(R.id.rankTitle);
        xptext = (TextView) encDialog.findViewById(R.id.xptext);
        chessRank = (ImageView) encDialog.findViewById(R.id.chessRank);
        String xxp = String.valueOf(xp);
        xptext.setText("+"+xxp+" xp");

        if(uploadXP>=0 && uploadXP<6000){
           chessRank.setImageResource(R.drawable.ic_chess_pawn);
           int color = Color.parseColor("#3eb959");
           chessRank.setColorFilter(color);
           rankTitle.setText("You're now a Pawn!");
        }else if(uploadXP>=6000 && uploadXP<10000){
            chessRank.setImageResource(R.drawable.ic_chess_knight);
            int color = Color.parseColor("#3eb959");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Knight!");
        }else if(uploadXP>=10000 && uploadXP<13000){
            chessRank.setImageResource(R.drawable.ic_chess_bishop);
            int color = Color.parseColor("#3eb959");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Bishop!");
        }else if(uploadXP>=13000 && uploadXP<16000){
            chessRank.setImageResource(R.drawable.ic_chess_rook);
            int color = Color.parseColor("#3eb959");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Rook!");
        }else if(uploadXP>=16000 && uploadXP<20000){
            chessRank.setImageResource(R.drawable.ic_chess_queen);
            int color = Color.parseColor("#3eb959");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Queen!");
        }else if(uploadXP>=20000){
            chessRank.setImageResource(R.drawable.ic_chess_king);
            int color = Color.parseColor("#3eb959");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a King!");
        }

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encDialog.dismiss();
            }
        });
        encDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        encDialog.show();

    }

    public void encMedium(long xp, long uploadXP) {
        encDialog = new Dialog(this);
        encDialog.setContentView(R.layout.enc_word_medium);
        closebtn = (ImageView) encDialog.findViewById(R.id.closebtn);
        rankTitle = (TextView) encDialog.findViewById(R.id.rankTitle);
        chessRank = (ImageView) encDialog.findViewById(R.id.chessRank);
        xptext = (TextView) encDialog.findViewById(R.id.xptext);
        String xxp = String.valueOf(xp);
        xptext.setText("+"+xxp+" xp");

        if(uploadXP>=1 && uploadXP<6000){
            chessRank.setImageResource(R.drawable.ic_chess_pawn);
            int color = Color.parseColor("#ffc61a");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Pawn!");
        }else if(uploadXP>=6000 && uploadXP<10000){
            chessRank.setImageResource(R.drawable.ic_chess_knight);
            int color = Color.parseColor("#ffc61a");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Knight!");
        }else if(uploadXP>=10000 && uploadXP<13000){
            chessRank.setImageResource(R.drawable.ic_chess_bishop);
            int color = Color.parseColor("#ffc61a");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Bishop!");
        }else if(uploadXP>=13000 && uploadXP<16000){
            chessRank.setImageResource(R.drawable.ic_chess_rook);
            int color = Color.parseColor("#ffc61a");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Rook!");
        }else if(uploadXP>=16000 && uploadXP<20000){
            chessRank.setImageResource(R.drawable.ic_chess_queen);
            int color = Color.parseColor("#ffc61a");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Queen!");
        }else if(uploadXP>=20000){
            chessRank.setImageResource(R.drawable.ic_chess_king);
            int color = Color.parseColor("#ffc61a");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a King!");
        }


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encDialog.dismiss();
            }
        });
        encDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        encDialog.show();

    }

    public void encLow(long xp, long uploadXP) {
        encDialog = new Dialog(this);
        encDialog.setContentView(R.layout.enc_word_low);
        closebtn = (ImageView) encDialog.findViewById(R.id.closebtn);
        rankTitle = (TextView) encDialog.findViewById(R.id.rankTitle);
        chessRank = (ImageView) encDialog.findViewById(R.id.chessRank);
        xptext = (TextView) encDialog.findViewById(R.id.xptext);
        String xxp = String.valueOf(xp);
        xptext.setText("+"+xxp+" xp");

        if(uploadXP>=1 && uploadXP<6000){
            chessRank.setImageResource(R.drawable.ic_chess_pawn);
            int color = Color.parseColor("#538ac6");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Pawn!");
        }else if(uploadXP>=6000 && uploadXP<10000){
            chessRank.setImageResource(R.drawable.ic_chess_knight);
            int color = Color.parseColor("#538ac6");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Knight!");
        }else if(uploadXP>=10000 && uploadXP<13000){
            chessRank.setImageResource(R.drawable.ic_chess_bishop);
            int color = Color.parseColor("#538ac6");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Bishop!");
        }else if(uploadXP>=13000 && uploadXP<16000){
            chessRank.setImageResource(R.drawable.ic_chess_rook);
            int color = Color.parseColor("#538ac6");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Rook!");
        }else if(uploadXP>=16000 && uploadXP<20000){
            chessRank.setImageResource(R.drawable.ic_chess_queen);
            int color = Color.parseColor("#538ac6");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a Queen!");
        }else if(uploadXP>=20000){
            chessRank.setImageResource(R.drawable.ic_chess_king);
            int color = Color.parseColor("#538ac6");
            chessRank.setColorFilter(color);
            rankTitle.setText("You're now a King!");
        }


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encDialog.dismiss();
            }
        });
        encDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        encDialog.show();

    }

    public void ChallengeBGM(View view){
        Intent intent = new Intent(Score.this, ChallengeBGM.class);
        stopService(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Category.class);
        startActivity(intent);
    }

}
