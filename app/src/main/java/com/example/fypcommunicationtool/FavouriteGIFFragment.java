package com.example.fypcommunicationtool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FavouriteGIFFragment extends Fragment {

    private View GIFView;
    private RecyclerView myGIFList;
    private SearchView searchView;

    private DatabaseReference GIFRef, favlistRef;
    private FirebaseAuth mAuth;
    private String currentUserID="";



    ArrayList<GIF> list;

    public FavouriteGIFFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        GIFView = inflater.inflate(R.layout.fragment_favourite_gif, container, false);
        myGIFList = (RecyclerView) GIFView.findViewById(R.id.fav_gif_list);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();

        GIFRef = FirebaseDatabase.getInstance().getReference().child("SignLanguageGIF");
        favlistRef = FirebaseDatabase.getInstance().getReference("FavouriteGIF").child(currentUserID);

        return GIFView;
    }
    @Override
    public void onStart() {

        super.onStart();

        if(favlistRef != null){
            favlistRef.orderByChild("malayCaption").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        list = new ArrayList<>();

                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            list.add(ds.getValue(GIF.class));
                        }

                        FavGIFAdapter favgifAdapter = new FavGIFAdapter(getActivity(),list);
                        myGIFList.setLayoutManager(new GridLayoutManager(getActivity(),3));
                        myGIFList.setAdapter(favgifAdapter);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }


    }


}