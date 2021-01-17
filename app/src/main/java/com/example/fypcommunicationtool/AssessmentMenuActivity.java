package com.example.fypcommunicationtool;

import android.os.Bundle;

public class AssessmentMenuActivity extends BaseActivity {

    private static final String TAG = "AssessmentMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AssessmentHomeFragment()).commit();
        }
//        getSupportFragmentManager().addOnBackStackChangedListener(this);

    }

}