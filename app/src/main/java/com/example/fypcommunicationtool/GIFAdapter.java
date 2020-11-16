package com.example.fypcommunicationtool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class GIFAdapter extends RecyclerView.Adapter<com.example.fypcommunicationtool.GIFAdapter.GIFViewHolder> {

    private List<com.example.fypcommunicationtool.GIF> gifList;
    private FirebaseAuth mAuth;
    private DatabaseReference GIFRef, favlistRef;
    private String userID;
    Activity t;

    public GIFAdapter (Activity t, List<com.example.fypcommunicationtool.GIF> gifList)
    {
        this.t = t;
        this.gifList = gifList;
    }


    public class GIFViewHolder extends RecyclerView.ViewHolder
    {
        public TextView engCaption, malayCaption;
        public WebView gifPicture;
        public CardView cardView;
        public ImageButton unfavbtn;


        public GIFViewHolder(@NonNull View itemView)
        {
            super(itemView);


            mAuth = FirebaseAuth.getInstance();
            userID = mAuth.getCurrentUser().getUid();
            GIFRef = FirebaseDatabase.getInstance().getReference().child("SignLanguageGIF");
            favlistRef = FirebaseDatabase.getInstance().getReference("FavouriteGIF");

            gifPicture = (WebView) itemView.findViewById(R.id.gifPicture);
            engCaption = (TextView) itemView.findViewById(R.id.engCaption);
            malayCaption = (TextView) itemView.findViewById(R.id.malayCaption);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
            unfavbtn = (ImageButton) itemView.findViewById(R.id.fav_gif);

            //set fav button red filled if exist in favlist
            favlistRef.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(gifList.get(getAdapterPosition()).getEngCaption())){
                        unfavbtn.setImageResource(R.drawable.ic_favorite);
//                        favlistRef.removeEventListener(this);
                        favlistRef.child(userID).addListenerForSingleValueEvent(this);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

                //add to fav
                unfavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        GIF gif = gifList.get(position);
                        unfavbtn.setImageResource(R.drawable.ic_favorite);
                        GIFRef.orderByChild("malayCaption").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String malaycap = gif.getMalayCaption();
                                String engcap = gif.getEngCaption();
                                String pic = gif.getGifPicture();
                                String cat = gif.getCategory();

                                GIF addfavgif = new GIF(engcap, malaycap, pic, cat);
                                favlistRef.child(userID).child(engcap).setValue(addfavgif);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });


        }

    }



    @NonNull
    @Override
    public GIFViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_gif, viewGroup, false);
        mAuth = FirebaseAuth.getInstance();

        return new GIFViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GIFViewHolder gifViewHolder, int i) {

        gifViewHolder.gifPicture.loadUrl(gifList.get(i).getGifPicture());
        gifViewHolder.gifPicture.getSettings().setLoadWithOverviewMode(true);
        gifViewHolder.gifPicture.getSettings().setUseWideViewPort(true);
        gifViewHolder.engCaption.setText(gifList.get(i).getEngCaption());
        gifViewHolder.malayCaption.setText(gifList.get(i).getMalayCaption());

        //click to enlarge gif n send
        gifViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String gif = gifList.get(i).getGifPicture();
                final Dialog gifDialog = new Dialog(t);
                gifDialog.setContentView(R.layout.enlarge_gif);
                WebView wb = (WebView) gifDialog.findViewById(R.id.bigGif);
                wb.loadUrl(gif);
                wb.getSettings().setLoadWithOverviewMode(true);
                wb.getSettings().setUseWideViewPort(true);

                gifDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                gifDialog.show();

            }

        });
        

    }

    @Override
    public int getItemCount() {
        return gifList.size();
    }

}
