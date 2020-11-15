package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class AssessmentMenu_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, FragmentManager.OnBackStackChangedListener {

    private TextView actionBarTitle;
    Boolean doubleBackToExitPressedOnce = false;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_menu_);
//        this.setTitle("Assessment");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        actionBarTitle = findViewById(R.id.toolbar_title);
        drawer = findViewById(R.id.nav_drawer_layout);
        ImageButton rightMenu = findViewById(R.id.ic_right_menu);
        NavigationView navigationView = findViewById(R.id.nav_view);

        initBottomBar ibb = new initBottomBar(this);
        ibb.setNavigation(this);
        rightMenu.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AssessmentHomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);

    }

//    void launchDrawer() {
//        navDrawer drawer = new navDrawer(this, getApplicationContext());
////        drawer.setDrawerLayout();
//    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.app_menu, menu);
//        return true;
//    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                selectedFragment = new AssessmentHomeFragment();
                break;
//            case R.id.nav_profile:
//                selectedFragment = new ProfileFragment();
//                break;
//            case R.id.nav_notifications:
//                selectedFragment = new NotificationsFragment();
//                break;
            default:
                return super.onOptionsItemSelected(item);
        }



        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                selectedFragment = new AssessmentHomeFragment();
                break;
//            case R.id.nav_assessment:
//                selectedFragment = new AssessmentMenu_Fragment();
//                break;
//            case R.id.nav_administration:
//                selectedFragment = new AdministrationMenu_Fragment();
//                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();
        drawer.closeDrawer(GravityCompat.END);
        return true;
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

