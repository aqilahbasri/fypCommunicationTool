package com.example.fypcommunicationtool;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FavGIFAdapter extends RecyclerView.Adapter<FavGIFAdapter.FavGIFViewHolder> {

    private List<GIF> gifList;
    private FirebaseAuth mAuth;
    private DatabaseReference GIFRef, favlistRef;
    private String userID;
    Activity t;

    public FavGIFAdapter(Activity t,List<GIF> gifList)
    {
        this.t = t;
        this.gifList = gifList;
    }

    public class FavGIFViewHolder extends RecyclerView.ViewHolder
    {
        public TextView engCaption, malayCaption;
        public WebView gifPicture;
        public CardView cardView;
        public ImageButton unfavbtn;
        boolean favcheker = false;


        public FavGIFViewHolder(@NonNull View itemView)
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
            unfavbtn.setImageResource(R.drawable.ic_favorite);


            //remove from fav
            unfavbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    GIF gif = gifList.get(position);
                    unfavbtn.setImageResource(R.drawable.ic_unfavorite);
                    GIFRef.orderByChild("malayCaption").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String malaycap = gif.getMalayCaption();
                            String engcap = gif.getEngCaption();
                            String pic = gif.getGifPicture();
                            String cat = gif.getCategory();

                            GIF addfavgif = new GIF(engcap, malaycap, pic, cat);
                            favlistRef.child(userID).child(engcap).removeValue();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {


                        }
                    });

                }
            });

        }

    }

    public FavGIFViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_gif, viewGroup, false);
        mAuth = FirebaseAuth.getInstance();

        return new FavGIFViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavGIFViewHolder gifViewHolder, int position) {
        
        gifViewHolder.gifPicture.loadUrl(gifList.get(position).getGifPicture());
        gifViewHolder.gifPicture.getSettings().setLoadWithOverviewMode(true);
        gifViewHolder.gifPicture.getSettings().setUseWideViewPort(true);
        gifViewHolder.engCaption.setText(gifList.get(position).getEngCaption());
        gifViewHolder.malayCaption.setText(gifList.get(position).getMalayCaption());

        //click to enlarge gif n send
        gifViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String gif = gifList.get(position).getGifPicture();
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
