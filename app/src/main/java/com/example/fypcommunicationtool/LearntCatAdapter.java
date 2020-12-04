package com.example.fypcommunicationtool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LearntCatAdapter extends RecyclerView.Adapter<LearntCatAdapter.MyViewHolder> {

    Context context;
    public static ArrayList<CategoryList> categories;

    public LearntCatAdapter (Context c, ArrayList<CategoryList> p){
        context =c;
        categories = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LearntCatAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardviewlearnt,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LearntCatAdapter.MyViewHolder holder, int position) {
        Picasso.get().load(categories.get(position).getCategoryimage()).into(holder.categoryimage);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryimage;

        public MyViewHolder(View itemView) {
            super(itemView);
            categoryimage = (ImageView) itemView.findViewById(R.id.categoryimage);

        }
    }
}
