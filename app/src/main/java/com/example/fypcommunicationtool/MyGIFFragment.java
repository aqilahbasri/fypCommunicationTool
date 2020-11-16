package com.example.fypcommunicationtool;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

public class MyGIFFragment extends Fragment {

    private View GIFView;
    private RecyclerView myGIFList;
    private SearchView searchView;

    private DatabaseReference GIFRef, favlistRef;
    private FirebaseAuth mAuth;
    private String currentUserID="";



    ArrayList<GIF> list;

    public MyGIFFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        GIFView = inflater.inflate(R.layout.fragment_my_gif, container, false);
        myGIFList = (RecyclerView) GIFView.findViewById(R.id.gif_list);
        searchView = (SearchView) GIFView.findViewById(R.id.search_bar);

//        getParentFragmentManager().beginTransaction().detach(this).attach(this).commit();

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

        if(GIFRef != null){
            GIFRef.orderByChild("malayCaption").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        list = new ArrayList<>();

                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            list.add(ds.getValue(GIF.class));
                        }

                        GIFAdapter gifAdapter = new GIFAdapter(getActivity(),list);
                        myGIFList.setLayoutManager(new GridLayoutManager(getActivity(),3));
                        myGIFList.setAdapter(gifAdapter);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }

        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    private void search(String s) {
        ArrayList<GIF> myList = new ArrayList<>();
//        boolean resultExist = false;

        for(GIF gif : list){
            if(gif.getEngCaption().toLowerCase().contains(s.toLowerCase()) || gif.getMalayCaption().toLowerCase().contains(s.toLowerCase())){
                myList.add(gif);
//                resultExist = true;
            }

        }

        if(myList.isEmpty()){
            String[] sw = s.split(" "); //search word
            ArrayList<String> sc = new ArrayList<>(); //search category

            //recommendation by word
            for(String searchWord : sw){
                for(GIF gif : list){
                    if(gif.getEngCaption().toLowerCase().contains(searchWord.toLowerCase()) || gif.getMalayCaption().toLowerCase().contains(searchWord.toLowerCase())){
                        myList.add(gif);
                        if(sc.contains(gif.getCategory())){

                        }
                        else{
                            sc.add(gif.getCategory());
                        }
                    }
                }
            }

            //recommendation by category
            for(String searchCategory : sc){
                for(GIF gif : list){
                    if(gif.getCategory().contains(searchCategory.toLowerCase())){
                        myList.add(gif);
                    }
                }
            }


        }

        GIFAdapter gifAdapter = new GIFAdapter(getActivity(), myList);
        myGIFList.setAdapter(gifAdapter);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        // Refresh tab data:

        if (getFragmentManager() != null) {

            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }
}