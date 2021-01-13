package com.example.fypcommunicationtool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class TestSettingsLevelAdapter extends FirestoreRecyclerAdapter<TestSettingsLevelModel, TestSettingsLevelAdapter.MyViewHolder> {

    Activity activity;
    private static final String TAG = "TestSettingsLevelAdapter";

    public TestSettingsLevelAdapter(@NonNull FirestoreRecyclerOptions<TestSettingsLevelModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_test_level, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, @NonNull final TestSettingsLevelModel model) {

        holder.levelName.setText(model.getLevelName());
        String iconUrl = model.getLevelIconUrl();
        Picasso.get().load(iconUrl).into(holder.levelIcon);

        holder.levelCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StartAssessmentActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AssessmentInstructionsFragment(getSnapshots().getSnapshot(position)
                                .getReference().getPath())).addToBackStack(null).commit();
            }
        });

    }

    protected void setActivity(Activity activity) {
        this.activity = activity;
    }

    protected Activity getActivity() {
        return activity;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView levelCardView;
        TextView levelName;
        ImageView levelIcon;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            levelCardView = itemView.findViewById(R.id.level_cardView);
            levelName = itemView.findViewById(R.id.level_name);
            levelIcon = itemView.findViewById(R.id.level_icon);
        }
    }

}

class TestSettingsLevelModel {

    String levelName, levelIconUrl;
    Long dateAdded, dateModified, duration;
    int overallPassingMark;

    public TestSettingsLevelModel() {
    }

    public TestSettingsLevelModel(String levelName, String levelIconUrl, Long dateAdded, Long dateModified, Long duration, int overallPassingMark) {
        this.levelName = levelName;
        this.levelIconUrl = levelIconUrl;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.duration = duration;
        this.overallPassingMark = overallPassingMark;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelIconUrl() {
        return levelIconUrl;
    }

    public void setLevelIconUrl(String levelIconUrl) {
        this.levelIconUrl = levelIconUrl;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Long getDateModified() {
        return dateModified;
    }

    public void setDateModified(Long dateModified) {
        this.dateModified = dateModified;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public int getOverallPassingMark() {
        return overallPassingMark;
    }

    public void setOverallPassingMark(int overallPassingMark) {
        this.overallPassingMark = overallPassingMark;
    }
}
