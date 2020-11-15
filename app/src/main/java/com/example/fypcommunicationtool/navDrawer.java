package com.example.fypcommunicationtool;

import android.app.Activity;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class navDrawer extends AssessmentMenu_Activity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Activity activity;
    private DrawerLayout drawer;
    ImageButton rightMenu;
    boolean doubleBackToExitPressedOnce = false;
    NavigationView navigationView;

    navDrawer(Activity activity, NavigationView navigationView) {
        this.activity = activity;
        this.navigationView = navigationView;
//        this.drawer = drawer;
    }

    public void initNavDrawer() {
        rightMenu = activity.findViewById(R.id.ic_right_menu);
        drawer = activity.findViewById(R.id.nav_drawer_layout);
        rightMenu.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
//            case R.id.nav_assessment:
//                selectedFragment = new AssessmentMenu_Fragment();
//                break;
//            case R.id.nav_administration:
//                selectedFragment = new AdministrationMenu_Fragment();
//                break;
        }

        ((MainActivity) activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onBackPressed() {
//        moveTaskToBack(true);

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }

        else if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
            moveTaskToBack(true);
        }

        else {
            doubleBackToExitPressedOnce = true;
            Toast.makeText(activity, "Press again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
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
}
