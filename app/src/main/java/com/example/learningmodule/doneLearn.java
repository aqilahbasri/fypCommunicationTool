package com.example.learningmodule;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class doneLearn extends AppCompatActivity implements View.OnClickListener {

    Button backhome;
    ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_learn);

        toolbar = getSupportActionBar();
        toolbar.hide();

        backhome = (Button) findViewById(R.id.backhome);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick();
            }

            private void onClick() {
                Intent intent = new Intent(doneLearn.this, MainActivityLearning.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}