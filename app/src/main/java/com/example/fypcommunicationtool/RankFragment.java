package com.example.fypcommunicationtool;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.google.firebase.database.FirebaseDatabase.*;

public class RankFragment extends Fragment {

    private RankViewModel mViewModel;
   // private View RankView;
    private DatabaseReference databaseReference, userdetailRef;
    private RecyclerView ranklistrecycler;
    private FirebaseAuth mAuth;
    private String userID, username;

    ArrayList<UploadScore> list;
    RecyclerView.LayoutManager mLayoutManager;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    public RankFragment(){}


    public static RankFragment newInstance() {
        return new RankFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View RankView = inflater.inflate(R.layout.rank_fragment, container, false);


        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        ranklistrecycler = (RecyclerView) RankView.findViewById(R.id.rank_list);
        ranklistrecycler.setLayoutManager(layoutManager);

        list = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        userdetailRef = FirebaseDatabase.getInstance().getReference("Users");

        //retrieve username n profilepic
        userdetailRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("userID").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("LEADERBOARD");
        databaseReference.orderByChild("xp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    UploadScore p = dataSnapshot1.getValue(UploadScore.class);
                    list.add(p);
                }
                adapter = new RankAdapter(getContext(), list, username);
                ranklistrecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }

        });

        return RankView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RankViewModel.class);
        // TODO: Use the ViewModel
    }

}
