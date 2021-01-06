package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.navigation.NavigationView;

public class MainActivityLearning extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private TextView actionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_learning);

        BottomNavigationView navigation = findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar = (Toolbar) findViewById(R.id.main_learning_toolbar);
        setSupportActionBar(toolbar);


//        yvonne punya toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarTitle = findViewById(R.id.toolbar_title);
        actionBarTitle.setText("LEARNING");
        ImageButton rightMenu = findViewById(R.id.ic_right_menu);
        rightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });

        loadFragment(new CategoryFragment());

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        goToMainActivity();
                        break;
                    case R.id.nav_learning:
                        goToMainLearningModule();
                        break;
                    case R.id.nav_assessment:
                        goToMainAssessmentModule();
                        break;
                }
                drawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });



    }

    private void goToMainLearningModule() {
        Intent mainLearningModule = new Intent(this, MainActivityLearning.class);
        startActivity(mainLearningModule);
    }

    private void goToMainAssessmentModule() {
        Intent mainAssessmentModule = new Intent(this, AssessmentMenuActivity.class);
        startActivity(mainAssessmentModule);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.learn:
                    toolbar.setTitle("LEARNING");
                    loadFragment(new CategoryFragment());
                    return true;

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

    private void goToMainActivity() {
        Intent mainCommunicationModule = new Intent(this, MainActivity.class);
        startActivity(mainCommunicationModule);
    }

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
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            Intent intent = new Intent(this, MainActivityLearning.class);
            startActivity(intent);
        }

    }



}
