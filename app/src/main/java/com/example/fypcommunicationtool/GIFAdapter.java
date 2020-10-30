package com.example.fypcommunicationtool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class GIFAdapter extends RecyclerView.Adapter<com.example.fypcommunicationtool.GIFAdapter.GIFViewHolder> {

    private List<com.example.fypcommunicationtool.GIF> gifList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public GIFAdapter (List<com.example.fypcommunicationtool.GIF> gifList)
    {
        this.gifList = gifList;
    }

    public class GIFViewHolder extends RecyclerView.ViewHolder
    {
        public TextView engCaption, malayCaption;
        public GifImageView gifPicture;
        public CardView cardView;

        public GIFViewHolder(@NonNull View itemView)
        {
            super(itemView);

            gifPicture = (GifImageView) itemView.findViewById(R.id.gifPicture);
            engCaption = (TextView) itemView.findViewById(R.id.engCaption);
            malayCaption = (TextView) itemView.findViewById(R.id.malayCaption);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
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

        Picasso.get().load(gifList.get(i).getGifPicture()).into(gifViewHolder.gifPicture);
        gifViewHolder.engCaption.setText(gifList.get(i).getEngCaption());
        gifViewHolder.malayCaption.setText(gifList.get(i).getMalayCaption());

    }

    @Override
    public int getItemCount() {
        return gifList.size();
    }
}