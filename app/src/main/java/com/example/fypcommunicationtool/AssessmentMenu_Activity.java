package com.example.fypcommunicationtool;

import android.os.Bundle;

public class AssessmentMenu_Activity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AssessmentHomeFragment()).commit();
        }
//        getSupportFragmentManager().addOnBackStackChangedListener(this);

    }
}