package com.example.learningmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Score extends AppCompatActivity implements View.OnClickListener{

    private Button backhome;
    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

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
        String data = extras.getString("finalscore");
        score.setText(data + "%");
    }

    public void backhome() {
        Intent intent = new Intent(this, MainActivityLearning.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
