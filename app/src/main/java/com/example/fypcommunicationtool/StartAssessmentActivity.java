package com.example.fypcommunicationtool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StartAssessmentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new StartAssessmentFragment()).commit();
        }
    }
}