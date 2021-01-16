package com.example.fypcommunicationtool;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.fypcommunicationtool.utilities.Constants;
import com.example.fypcommunicationtool.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private TextView actionBarTitle;

    private Toolbar mToolbar;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawer2;
    Boolean doubleBackToExitPressedOnce = false;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();
        preferenceManager = new PreferenceManager(getApplicationContext());

//        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("Communication");

        //        yvonne punya toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarTitle = findViewById(R.id.toolbar_title);
        actionBarTitle.setText("Communication");
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

        setDefaultFragment();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        NavigationView navigationView = findViewById(R.id.nav_view);

        if (currentUser != null)
            new InitNavDrawerHeader(navigationView, TAG);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        goToMainCommunicationModule();
                        break;
                    case R.id.nav_learning:
                        goToMainLearningModule();
                        break;
                    case R.id.nav_assessment:
                        goToMainAssessmentModule();
                        break;
                    case R.id.sign_out:
//                        mAuth.signOut();
                        userSignOut(mAuth);
                        goToLoginActivity();
                        break;
                    case R.id.setting_profile:
                        goToSettingActivity();
                        break;
                }
                drawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });


//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,
                    new ChatsFragment()).commit();
        }

        bottomNavigationView = findViewById(R.id.main_tabs);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.chat_menu:
                        loadFragment(new ChatsFragment());
                        return true;
                    case R.id.library_menu:
                        loadFragment(new GIFLibraryFragment());
                        return true;
                    case R.id.contact_menu:
                        loadFragment(new ContactsFragment());
                        return true;

                }
                return false;
//                Fragment selectedFragment = null;
//
//                switch(menuItem.getItemId()){
//                    case R.id.chat_menu : selectedFragment = new ChatsFragment(); break;
//                    case R.id.library_menu : selectedFragment = new GIFLibraryFragment(); break;
//                    case R.id.contact_menu : selectedFragment = new ContactsFragment(); break;
//                }
//
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.main_frame, selectedFragment);
//                transaction.commit();
//
//
//                return false;
            }
            private void loadFragment(Fragment fragment) {
                // load fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, fragment);
//        transaction.addToBackStack(null);
                transaction.disallowAddToBackStack();
                transaction.commit();


            }
        });

    }

    private void userSignOut(FirebaseAuth mAuth) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child(Constants.KEY_COLLECTION_USERS)
                .child(preferenceManager.getString(Constants.KEY_USER_ID));
        reference.child(Constants.KEY_FCM_TOKEN).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e(TAG, "Successfully sign out");
                    mAuth.signOut();
                }
            }
        });
    }

    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, ChatsFragment.getInstance());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_home: //nanti tukar Fatin punya
                goToMainCommunicationModule();
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
//            case R.id.nav_administration:
//                selectedFragment = new AdministrationMenu_Fragment();
//                break;
        }
//
////        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onBackPressed() {
//
//        if (drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
//        }
//
//        else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//        }
//
//        else if (isTaskRoot()) {    //Only can exit from Home fragment
//            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
//            }
//            else {
//                doubleBackToExitPressedOnce = true;
//                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        doubleBackToExitPressedOnce=false;
//                    }
//                }, 2000);
//            }
//        }
//
//        else {
//            super.onBackPressed();
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser == null){
            goToLoginActivity();
        }
    }

    private void goToLoginActivity() {
        Intent loginIntent = new Intent(com.example.fypcommunicationtool.MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    private void goToSettingActivity() {
        Intent settingIntent = new Intent(com.example.fypcommunicationtool.MainActivity.this, SettingActivity.class);
        startActivity(settingIntent);
    }

    private void SendUserToFindFriendsActivity() {
        Intent findFriendsIntent = new Intent(com.example.fypcommunicationtool.MainActivity.this, FindFriendsActivity.class);
        startActivity(findFriendsIntent);
    }

    private void goToMainLearningModule() {
        Intent mainLearningModule = new Intent(com.example.fypcommunicationtool.MainActivity.this, MainActivityLearning.class);
        startActivity(mainLearningModule);
    }

    private void goToMainAssessmentModule() {
        Intent mainAssessmentModule = new Intent(com.example.fypcommunicationtool.MainActivity.this, AssessmentMenuActivity.class);
        startActivity(mainAssessmentModule);
    }

    private void goToMainCommunicationModule() {
        Intent mainCommunicationModule = new Intent(com.example.fypcommunicationtool.MainActivity.this, MainActivity.class);
        startActivity(mainCommunicationModule);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.options_menu, menu);
//        return true;
//    }

// @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        super.onOptionsItemSelected(item);
//
//        if(item.getItemId()==R.id.menu_communication_module){
//
//        }
//        if(item.getItemId()==R.id.menu_add_group){
//            RequestNewGroup();
//        }
//        if(item.getItemId()==R.id.menu_find_friends){
//            SendUserToFindFriendsActivity();
//        }
//        if(item.getItemId()==R.id.menu_learning_module){
//            goToMainLearningModule();
//        }
//        if(item.getItemId()==R.id.menu_assessment_module){
//            goToMainAssessmentModule();
//        }
//        if(item.getItemId()==R.id.menu_setting){
//            SendUserToSettingActivity();
//        }
//        if(item.getItemId()==R.id.menu_signout){
//            mAuth.signOut();
//            SendUserToLoginActivity();
//        }
//
//        return true;
//    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(com.example.fypcommunicationtool.MainActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter group name : ");
        final EditText groupNameField = new EditText(com.example.fypcommunicationtool.MainActivity.this);
        groupNameField.setHint("e.g Group A");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();

                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(com.example.fypcommunicationtool.MainActivity.this, "Please insert group name.", Toast.LENGTH_SHORT);
                }
                else{
                    CreateNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void CreateNewGroup(final String groupName) {
        RootRef.child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(com.example.fypcommunicationtool.MainActivity.this, groupName+" group is created.", Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
