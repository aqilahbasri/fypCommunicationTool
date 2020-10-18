package com.example.learningmodule;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryFragment extends Fragment implements View.OnClickListener {


    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<CategoryList> list;
   // MyAdapter adapter;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.LayoutManager mLayoutManager;

    private CategoryViewModel mViewModel;

    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         final View view = inflater.inflate(R.layout.category_fragment, container, false);

        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(mLayoutManager);
        list = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("LEARNING");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    CategoryList p = dataSnapshot1.getValue(CategoryList.class);
                    list.add(p);
                }
                adapter = new MyAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;

    }

    @Override
    public void onClick(View v) {

    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
//        // TODO: Use the ViewModel
//
//
//
//    }

}
