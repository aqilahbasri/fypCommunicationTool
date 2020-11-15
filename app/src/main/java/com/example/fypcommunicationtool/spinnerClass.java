package com.example.fypcommunicationtool;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class spinnerClass implements AdapterView.OnItemSelectedListener {

    private  Context context;
    private Spinner state;

    spinnerClass(Context context, Spinner state) {
        this.context = context;
        this.state = state;
    }

    void spinnerActivity() {

        List<String> stateList = new ArrayList<>();
        stateList.add("Johor");
        stateList.add("Kedah");
        stateList.add("Kelantan");
        stateList.add("Melaka");
        stateList.add("Negeri Sembilan");
        stateList.add("Pahang");
        stateList.add("Perak");
        stateList.add("Perlis");
        stateList.add("Pulau Pinang");
        stateList.add("Sabah");
        stateList.add("Sarawak");
        stateList.add("Selangor");
        stateList.add("Terengganu");
        stateList.add("WP Kuala Lumpur");
        stateList.add("WP Labuan");
        stateList.add("WP Putrajaya");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, stateList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(adapter);
        state.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(context, "Error retrieving states", Toast.LENGTH_SHORT).show();
    }

    String getItem() {
        return state.getSelectedItem().toString();
    }
}