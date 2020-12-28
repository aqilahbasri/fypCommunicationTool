package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenuItemView;

public class MainActivityLearning extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_learning);

        BottomNavigationView navigation = findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar = (Toolbar) findViewById(R.id.main_learning_toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle("LEARNING");
        loadFragment(new CategoryFragment());



    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.learn:
                    toolbar.setTitle("LEARNING");
                    loadFragment(new CategoryFragment());
                    return true;
//                case R.id.dictionary:
//                    toolbar.setTitle("DICTIONARY");
//                    loadFragment(new DictionaryFragment());
//                    return true;
                case R.id.rank:
                    toolbar.setTitle("LEADERBOARD");
                    loadFragment(new RankFragment());
                    return true;
                case R.id.setting:
                    toolbar.setTitle("PROFILE");
                    loadFragment(new SettingFragment());
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
//        transaction.addToBackStack(null);
        transaction.disallowAddToBackStack();
        transaction.commit();


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivityLearning.class);
        startActivity(intent);

    }
}
