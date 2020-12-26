package com.example.fypcommunicationtool;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.MyViewHolder> {

    Context context;
    public static ArrayList<UploadScore> list;
    String name;



    public RankAdapter(Context c, ArrayList<UploadScore> p, String username) {
        context =c;
        list = p;
        name = username;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_rank_display,parent,false));
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int lala = list.size()-position;

        if(list.get(position).getUsername().equals(name)){
            int color = Color.parseColor("#BA32AC");
            holder.linlayout.setBackgroundColor(color);
            holder.username.setTextColor(Color.WHITE);
            holder.xp.setTextColor(Color.WHITE);
            holder.number.setTextColor(Color.WHITE);
        }

        holder.username.setText(list.get(position).getUsername());
        holder.xp.setText(String.valueOf(list.get(position).getXp())+"xp");
        Picasso.get().load(list.get(position).getProfileimage()).into(holder.profileimage);
        holder.number.setText(String.valueOf(lala));
        long xp = list.get(position).getXp();
        if(xp>=1 && xp<6000){
            holder.rankimage.setImageResource(R.drawable.ic_chess_pawn);
            int color = Color.parseColor("#32ACBA");
            holder.rankimage.setColorFilter(color);
        }else if(xp>=6000 && xp<10000){
            holder.rankimage.setImageResource(R.drawable.ic_chess_knight);
            int color = Color.parseColor("#32ACBA");
            holder.rankimage.setColorFilter(color);
        }else if(xp>=10000 && xp<13000){
            holder.rankimage.setImageResource(R.drawable.ic_chess_bishop);
            int color = Color.parseColor("#32ACBA");
            holder.rankimage.setColorFilter(color);
        }else if(xp>=13000 && xp<16000){
            holder.rankimage.setImageResource(R.drawable.ic_chess_rook);
            int color = Color.parseColor("#32ACBA");
            holder.rankimage.setColorFilter(color);
        }else if(xp>=16000 && xp<20000){
            holder.rankimage.setImageResource(R.drawable.ic_chess_queen);
            int color = Color.parseColor("#32ACBA");
            holder.rankimage.setColorFilter(color);
        }else if(xp>=20000){
            holder.rankimage.setImageResource(R.drawable.ic_chess_king);
            int color = Color.parseColor("#32ACBA");
            holder.rankimage.setColorFilter(color);
        }



    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, xp, number;
        ImageView profileimage, rankimage;
        LinearLayout linlayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.user_profile_name);
            xp = (TextView) itemView.findViewById(R.id.user_xp);
            profileimage = (ImageView) itemView.findViewById(R.id.users_profile_image);
            rankimage = (ImageView) itemView.findViewById(R.id.users_rank_image);
            number = (TextView) itemView.findViewById(R.id.number);
            linlayout = (LinearLayout) itemView.findViewById(R.id.linearlayoutrank);

        }

    }
}
