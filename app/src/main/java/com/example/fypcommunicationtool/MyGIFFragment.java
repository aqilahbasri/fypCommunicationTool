package com.example.fypcommunicationtool;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class MyGIFFragment extends Fragment {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private View GIFView;
    private RecyclerView myGIFList, myGIFRecommendationList;
    private SearchView searchView;
    private TextView noResult;
    private ImageButton voiceButton;

    private DatabaseReference GIFRef;
    private FirebaseAuth mAuth;

    ArrayList<GIF> list;

    public MyGIFFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        GIFView = inflater.inflate(R.layout.fragment_my_gif, container, false);
        myGIFList = (RecyclerView) GIFView.findViewById(R.id.gif_list);
        myGIFRecommendationList = (RecyclerView) GIFView.findViewById(R.id.recommendation_list);
        searchView = (SearchView) GIFView.findViewById(R.id.search_bar);
        noResult = (TextView) GIFView.findViewById(R.id.no_result);
        voiceButton = (ImageButton) GIFView.findViewById(R.id.voice_btn);

        mAuth = FirebaseAuth.getInstance();

        GIFRef = FirebaseDatabase.getInstance().getReference().child("SignLanguageGIF");

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

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

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

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);

        }catch (Exception e){
            Toast.makeText(getActivity(),""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK) {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            if (matches != null) {
                if (matches.size() > 0) {
                    searchView.setQuery(matches.get(0), false);
                }
            }
        }
    }

    private void search(String s) {
        ArrayList<GIF> myList = new ArrayList<>();
        ArrayList<GIF> wordList = new ArrayList<>();
        ArrayList<GIF> categoryList = new ArrayList<>();
        ArrayList<GIFRecommendation> myRecommendationList = new ArrayList<>();
//        boolean resultExist = false;

        myGIFList.setVisibility(View.VISIBLE);
        myGIFRecommendationList.setVisibility(View.INVISIBLE);
        noResult.setVisibility(View.INVISIBLE);

        for(GIF gif : list){
            if(gif.getEngCaption().toLowerCase().contains(s.toLowerCase()) || gif.getMalayCaption().toLowerCase().contains(s.toLowerCase())){
                myList.add(gif);
//                resultExist = true;
            }

        }

        if(myList.isEmpty()){
            myGIFList.setVisibility(View.INVISIBLE);
            myGIFRecommendationList.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.VISIBLE);

            String[] sw = s.split(" "); //search word
            ArrayList<String> sc = new ArrayList<>(); //search category

            //recommendation by word
            for(String searchWord : sw){
                for(GIF gif : list){
                    if(gif.getEngCaption().toLowerCase().contains(searchWord.toLowerCase()) || gif.getMalayCaption().toLowerCase().contains(searchWord.toLowerCase())){
                        wordList.add(gif);
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
                    if(gif.getCategory().contains(searchCategory)){
                        categoryList.add(gif);
                    }
                }
            }

            myRecommendationList.add(new GIFRecommendation("Related categories", categoryList));
            myRecommendationList.add(new GIFRecommendation("Other results", wordList));

            GIFRecommendationAdapter gifRecommendationAdapter = new GIFRecommendationAdapter(getContext(), myRecommendationList);
            myGIFRecommendationList.setAdapter(gifRecommendationAdapter);

        }

        else{
            GIFAdapter gifAdapter = new GIFAdapter(getContext(),myList);
            myGIFList.setAdapter(gifAdapter);
        }

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