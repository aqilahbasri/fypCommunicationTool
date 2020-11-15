package com.example.fypcommunicationtool;

import android.os.Bundle;

public class ApplyCertificateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ApplyCertificateFragment()).commit();
        }
    }
}