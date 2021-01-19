package com.example.fypcommunicationtool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubmitCourseworkAdapter extends RecyclerView.Adapter<SubmitCourseworkAdapter.MyViewHolder> {

    ArrayList<ManageCourseworkModalClass> newApplicationList;
    private final Activity activity;

    SubmitCourseworkAdapter(Activity activity, ArrayList<ManageCourseworkModalClass> newApplicationList) {
        this.activity = activity;
        this.newApplicationList = newApplicationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_layout_submit_coursework, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.index.setText(String.valueOf(position+1));
        holder.courseworkName.setText(newApplicationList.get(position).getCourseworkName());
        holder.courseworkQuestion.setText(newApplicationList.get(position).getCourseworkQuestion());
        holder.dateCreated.setText(newApplicationList.get(position).getDateCreated());

        holder.getFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newApplicationList.get(position).getCourseworkFile()));
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newApplicationList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView index;
        TextView courseworkName;
        TextView courseworkQuestion;
        TextView dateCreated;
        Button getFile;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.textView1);
            courseworkName = itemView.findViewById(R.id.textView2);
            courseworkQuestion = itemView.findViewById(R.id.textView3);
            dateCreated = itemView.findViewById(R.id.textView4);
            getFile = itemView.findViewById(R.id.review_button);
        }
    }

}