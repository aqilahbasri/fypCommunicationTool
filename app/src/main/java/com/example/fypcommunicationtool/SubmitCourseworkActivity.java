package com.example.fypcommunicationtool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SubmitCourseworkActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SubmitCourseworkFragment()).commit();
        }
    }

    //To check if user successfully select file
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 20 && resultCode == RESULT_OK && data != null) {
//            SubmitCourseworkAdapter adapter = new SubmitCourseworkAdapter();
//            adapter.filepath = data.getData(); //return uri of selected file
//            adapter.notification.setVisibility(View.VISIBLE);
//            adapter.notification.setText("File has been selected: "+data.getData().getLastPathSegment());
//        } else {
//            Toast.makeText(getApplicationContext(), "Please select a file", Toast.LENGTH_SHORT).show();
//        }
//    }

}