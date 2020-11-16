package com.example.fypcommunicationtool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class GIFRecommendationAdapter extends RecyclerView.Adapter<GIFRecommendationAdapter.GIFViewHolder> {

    private Context context;
    private List<GIFRecommendation> recommendationList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public GIFRecommendationAdapter(Context context, List<GIFRecommendation> recommendationList) {
        this.recommendationList = recommendationList;
        this.context = context;
    }

    public class GIFViewHolder extends RecyclerView.ViewHolder
    {
        TextView categoryTitle;
        RecyclerView itemRecycler;

        public GIFViewHolder(@NonNull View itemView)
        {
            super(itemView);

            categoryTitle = itemView.findViewById(R.id.cat_title);
            itemRecycler = itemView.findViewById(R.id.item_recycler);
        }
    }

    @NonNull
    @Override
    public GIFRecommendationAdapter.GIFViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_gif_recommendation, viewGroup, false);
        mAuth = FirebaseAuth.getInstance();

        return new GIFRecommendationAdapter.GIFViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GIFViewHolder holder, int position) {
        holder.categoryTitle.setText(recommendationList.get(position).getCategoryTitle());
        setCatItemRecycler(holder.itemRecycler, recommendationList.get(position).getCategoryItemList());
    }

    @Override
    public int getItemCount() {
        return recommendationList.size();
    }

    private void setCatItemRecycler(RecyclerView recyclerView, List<GIF> gifList){

        GIFAdapter gifAdapter = new GIFAdapter(gifList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(gifAdapter);

    }

}
