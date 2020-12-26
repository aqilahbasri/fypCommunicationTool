package com.example.fypcommunicationtool;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        final String img = categories.get(position).getCategoryimage();

        //click here to go to category play learn
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(categories.get(position).getCategoryname().equals("ALPHABET")){
                    final Dialog playlearnDialog = new Dialog(context);
                    playlearnDialog.setContentView(R.layout.playlearnchoose);
                    Button learncat = (Button) playlearnDialog.findViewById(R.id.learn);
                    Button challcat = (Button) playlearnDialog.findViewById(R.id.challenge);
                    Button practcat = (Button) playlearnDialog.findViewById(R.id.practice);
                    final String catname = categories.get(position).getCategoryname();

                    learncat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, Learn.class);
                            intent.putExtra("catTitle", catname);
                            context.startActivity(intent);

                        }
                    });

                    challcat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, Questions.class);
                            intent.putExtra("catTitle", catname);
                            context.startActivity(intent);
                        }
                    });

                    practcat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(categories.get(position).getCategoryname().equals("ALPHABET")){
                                Intent intent = new Intent(context, recognition.class);
                                intent.putExtra("catTitle", catname);
                                context.startActivity(intent);
                            }else{
                                Intent intent = new Intent(context, Practice.class);
                                intent.putExtra("catTitle", catname);
                                context.startActivity(intent);
                            }



                        }
                    });

                    playlearnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    playlearnDialog.show();

                }else if(categories.get(position).getCategoryname().equals("NUMBER")){
                    final Dialog playlearnDialog = new Dialog(context);
                    playlearnDialog.setContentView(R.layout.playlearnchoose);
                    Button learncat = (Button) playlearnDialog.findViewById(R.id.learn);
                    Button challcat = (Button) playlearnDialog.findViewById(R.id.challenge);
                    Button practcat = (Button) playlearnDialog.findViewById(R.id.practice);
                    final String catname = categories.get(position).getCategoryname();

                    learncat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, Learn.class);
                            intent.putExtra("catTitle", catname);
                            context.startActivity(intent);

                        }
                    });

                    challcat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, Questions.class);
                            intent.putExtra("catTitle", catname);
                            context.startActivity(intent);
                        }
                    });

                    practcat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(categories.get(position).getCategoryname().equals("ALPHABET")){
                                Intent intent = new Intent(context, recognition.class);
                                intent.putExtra("catTitle", catname);
                                context.startActivity(intent);
                            }else{
                                Intent intent = new Intent(context, Practice.class);
                                intent.putExtra("catTitle", catname);
                                context.startActivity(intent);
                            }
                        }
                    });

                    playlearnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    playlearnDialog.show();
                }else{

                    final Dialog playlearnwoPracDialog = new Dialog(context);
                    playlearnwoPracDialog.setContentView(R.layout.playlearnwopractice);
                    Button learncat = (Button) playlearnwoPracDialog.findViewById(R.id.learn);
                    Button challcat = (Button) playlearnwoPracDialog.findViewById(R.id.challenge);
                    final String catname = categories.get(position).getCategoryname();

                    learncat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, Learn.class);
                            intent.putExtra("catTitle", catname);
                            context.startActivity(intent);

                        }
                    });

                    challcat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, Questions.class);
                            intent.putExtra("catTitle", catname);
                            context.startActivity(intent);
                        }
                    });

                    playlearnwoPracDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    playlearnwoPracDialog.show();
                }

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
//            int position = getAdapterPosition();
        }



    }
}
