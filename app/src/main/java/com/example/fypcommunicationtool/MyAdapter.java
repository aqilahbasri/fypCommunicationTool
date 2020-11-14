package com.example.fypcommunicationtool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    public static  ArrayList<CategoryList> categories;

    public MyAdapter (Context c, ArrayList<CategoryList> p){
        context =c;
        categories = p;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.categoryname.setText(categories.get(position).getCategoryname());
        Picasso.get().load(categories.get(position).getCategoryimage()).into(holder.categoryimage);
        final String title = categories.get(position).getCategoryname();



        //click here to go to category play learn
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You click " + categories.get(position).getCategoryname(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,PlayLearn.class);
                intent.putExtra("catTitle", title);
                context.startActivity(intent);

            }

        });


    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{

        TextView categoryname;
        ImageView categoryimage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryname = (TextView) itemView.findViewById(R.id.categoryname);
            categoryimage = (ImageView) itemView.findViewById(R.id.categoryimage);
        }



    }
}
