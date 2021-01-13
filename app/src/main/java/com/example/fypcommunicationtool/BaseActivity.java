package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "BaseActivity";
    private TextView actionBarTitle;
    Boolean doubleBackToExitPressedOnce = false;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        actionBarTitle = findViewById(R.id.toolbar_title);

        InitBottomBar ibb = new InitBottomBar(this);    //Class for bottom nav_graph
        ibb.setNavigation();

        drawer = findViewById(R.id.nav_drawer_layout);
        ImageButton rightMenu = findViewById(R.id.ic_right_menu);
        NavigationView navigationView = findViewById(R.id.nav_view);

        new InitNavDrawerHeader(navigationView, TAG);

        rightMenu.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            //bawah ni nanti tukar Fatin punya (communication)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AssessmentHomeFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_assessment);
//            this will probably screw up because we use activity instead of fragments, so above code aku comment
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_home: //nanti tukar Fatin punya
                goToMainActivity();
//                selectedFragment = new ChatsFragment();
                break;
            case R.id.nav_learning:
                goToMainLearningModule();
//                selectedFragment = new CategoryFragment();
                break;
            case R.id.nav_assessment:
                goToMainAssessmentModule();
//                selectedFragment = new AssessmentHomeFragment();
                break;
            case R.id.sign_out:
                goToLoginActivity();
                break;
            case R.id.setting_profile:
                goToSettingActivity();
                break;
        }

        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    private void goToMainActivity() {
        Intent mainCommunicationModule = new Intent(com.example.fypcommunicationtool.BaseActivity.this, MainActivity.class);
        startActivity(mainCommunicationModule);
    }

    private void goToMainLearningModule() {
        Intent mainLearningModule = new Intent(com.example.fypcommunicationtool.BaseActivity.this, MainActivityLearning.class);
        startActivity(mainLearningModule);
    }

    private void goToMainAssessmentModule() {
        Intent mainAssessmentModule = new Intent(com.example.fypcommunicationtool.BaseActivity.this, AssessmentMenuActivity.class);
        startActivity(mainAssessmentModule);
    }

    private void goToLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void goToSettingActivity() {
        Intent settingIntent = new Intent(this, SettingActivity.class);
        startActivity(settingIntent);
    }

    //For actions when PHONE back button is pressed
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }

        else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }

        else if (isTaskRoot()) {    //Only can exit from Home fragment
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            }
            else {
                doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
        }

        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            drawer.openDrawer(GravityCompat.END);
        }
    }

    //Set action bar title.
    //P.s. have to use TextView instead of getSupportActionBar because nav_graph drawer is set to the right.
    public void setTitle(final String title) {
        actionBarTitle.setText(title);
    }

    // Change the title back when the fragment is changed
    @Override
    public void onBackStackChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assert fragment != null;
        fragment.onResume();
    }
}