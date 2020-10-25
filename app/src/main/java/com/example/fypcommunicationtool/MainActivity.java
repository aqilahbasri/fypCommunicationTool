package com.example.fypcommunicationtool;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private BottomNavigationView bottomNavigationView;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Communication");

        bottomNavigationView = findViewById(R.id.main_tabs);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment selectedFragment = null;

                switch(menuItem.getItemId()){
                    case R.id.chat_menu : selectedFragment = new ChatsFragment(); break;
                    case R.id.library_menu : selectedFragment = new GIFLibraryFragment(); break;
                    case R.id.contact_menu : selectedFragment = new ContactsFragment(); break;
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, selectedFragment);
                transaction.commit();
                return false;
            }
        });

        setDefaultFragment();
    }

    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, ChatsFragment.getInstance());
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser == null){
            SendUserToLoginActivity();
        }
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(com.example.fypcommunicationtool.MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void SendUserToSettingActivity() {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.menu_communication_module){

        }
        if(item.getItemId()==R.id.menu_add_group){
            RequestNewGroup();
        }
        if(item.getItemId()==R.id.menu_find_friends){
            SendUserToFindFriendsActivity();
        }
        if(item.getItemId()==R.id.menu_learning_module){
            goToMainLearningModule();
        }
        if(item.getItemId()==R.id.menu_assessment_module){

        }
        if(item.getItemId()==R.id.menu_setting){
            SendUserToSettingActivity();
        }
        if(item.getItemId()==R.id.menu_signout){
            mAuth.signOut();
            SendUserToLoginActivity();
        }

        return true;
    }

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
