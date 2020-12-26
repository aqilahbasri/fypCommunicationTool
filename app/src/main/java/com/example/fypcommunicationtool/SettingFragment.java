package com.example.fypcommunicationtool;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fypcommunicationtool.BroadcastReceiver.AlarmReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {


    DatabaseReference reference, dbreference;
    RecyclerView recyclerView;
    ArrayList<CategoryList> list;
    // MyAdapter adapter;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.LayoutManager mLayoutManager;
    TextView userxp, rankname, timepicker;
    FirebaseAuth mAuth;
    String userID, xp;
    ImageView rankic;
    SwitchCompat switchCompat;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        userxp = (TextView) view.findViewById(R.id.userxp);
        rankname = (TextView) view.findViewById(R.id.rankname);
        rankic = (ImageView) view.findViewById(R.id.rankic);
        switchCompat = (SwitchCompat) view.findViewById(R.id.switchCompat);
        timepicker = (TextView) view.findViewById(R.id.timepicker);

        mLayoutManager = new GridLayoutManager(getActivity(), 5);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(mLayoutManager);
        list = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("LEADERBOARD");
        //retrieve user's xp
        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                xp = dataSnapshot.child("xp").getValue().toString();
                userxp.setText(xp);

                int XP = Integer.parseInt(xp);

                if(XP>=0 && XP<6000){
                    rankic.setImageResource(R.drawable.ic_chess_pawn);
                    int color = Color.parseColor("#32ACBA");
                    rankic.setColorFilter(color);
                    rankname.setText("Pawn");
                }else if(XP>=6000 && XP<10000){
                    rankic.setImageResource(R.drawable.ic_chess_knight);
                    int color = Color.parseColor("#32ACBA");
                    rankic.setColorFilter(color);
                    rankname.setText("Knight");
                }else if(XP>=10000 && XP<13000){
                    rankic.setImageResource(R.drawable.ic_chess_bishop);
                    int color = Color.parseColor("#32ACBA");
                    rankic.setColorFilter(color);
                    rankname.setText("Bishop");
                }else if(XP>=13000 && XP<16000){
                    rankic.setImageResource(R.drawable.ic_chess_rook);
                    int color = Color.parseColor("#32ACBA");
                    rankic.setColorFilter(color);
                    rankname.setText("Rook");
                }else if(XP>=16000 && XP<20000){
                    rankic.setImageResource(R.drawable.ic_chess_queen);
                    int color = Color.parseColor("#32ACBA");
                    rankic.setColorFilter(color);
                    rankname.setText("Queen");
                }else if(XP>=20000){
                    rankic.setImageResource(R.drawable.ic_chess_king);
                    int color = Color.parseColor("#32ACBA");
                    rankic.setColorFilter(color);
                    rankname.setText("King");
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //show learnt category
        dbreference = FirebaseDatabase.getInstance().getReference().child("LearntCategory");
        dbreference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    CategoryList p = dataSnapshot1.getValue(CategoryList.class);
                    list.add(p);
                }
                adapter = new LearntCatAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //switch on notification
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    notificationReminder();
                    Toast.makeText(getContext(), "on", Toast.LENGTH_SHORT).show();

                    //pick time
                    timepicker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openTimePicker();
                        }
                    });

                }else{
                    Toast.makeText(getContext(), "Notification reminder turned off", Toast.LENGTH_SHORT).show();
                    timepicker.setClickable(false);
                }
            }

            private void notificationReminder() {
                openTimePicker();
            }


        });

        //pick time
//        timepicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openTimePicker();
//            }
//        });




        return view;
    }

    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.timePickerTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                String amPm = null;
                int hr = 0;
                if (hourOfDay == 12) {
                    hr = hourOfDay;
                    amPm = "PM";
                } if(hourOfDay >= 13){
                    hr = hourOfDay -12;
                    amPm = "PM";
                }if(hourOfDay<12){
                    hr = hourOfDay;
                    amPm = "AM";
                }
                if(DateFormat.is24HourFormat(getContext())){
                    timepicker.setText(String.format("%02d:%02d", hourOfDay, minutes));
                }else{
                    timepicker.setText(String.format("%02d:%02d", hr, minutes)+ amPm);
                }

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minutes);
                calendar.set(Calendar.SECOND,0);

                Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

            }
        },  currentHour, currentMinute, DateFormat.is24HourFormat(getContext()));
        timePickerDialog.show();

    }

}