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
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.cardview.widget.CardView;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentTransaction;
        import androidx.recyclerview.widget.RecyclerView;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.List;

public class PendingGIFAdapter extends RecyclerView.Adapter<PendingGIFAdapter.PendingGIFViewHolder> {

    private List<GIF> gifList;
    private FirebaseAuth mAuth;
    private DatabaseReference GIFRef, favlistRef;
    private String userID;
    Activity t;

    public PendingGIFAdapter(Activity t,List<GIF> gifList)
    {
        this.t = t;
        this.gifList = gifList;
    }

    public class PendingGIFViewHolder extends RecyclerView.ViewHolder
    {
        public TextView engCaption, malayCaption;
        public WebView gifPicture;
        public CardView cardView;
        public ImageButton unfavbtn;
        boolean favcheker = false;


        public PendingGIFViewHolder(@NonNull View itemView)
        {
            super(itemView);


            mAuth = FirebaseAuth.getInstance();
            userID = mAuth.getCurrentUser().getUid();
            GIFRef = FirebaseDatabase.getInstance().getReference().child("SignLanguageGIF");
            favlistRef = FirebaseDatabase.getInstance().getReference("FavouriteGIF");

            gifPicture = (WebView) itemView.findViewById(R.id.gifPictureBasic);
            engCaption = (TextView) itemView.findViewById(R.id.engCaptionBasic);
            malayCaption = (TextView) itemView.findViewById(R.id.malayCaptionBasic);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id_basic);
        }

    }

    public PendingGIFViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_gif_basic, viewGroup, false);
        mAuth = FirebaseAuth.getInstance();

        return new PendingGIFViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PendingGIFViewHolder gifViewHolder, int position) {

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
                ImageView sharebtn = (ImageView) gifDialog.findViewById(R.id.sharebtn);

                wb.loadUrl(gif);
                wb.getSettings().setLoadWithOverviewMode(true);
                wb.getSettings().setUseWideViewPort(true);

                sharebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(t, ContactsToSendGIf.class);
                        intent.putExtra("gifurl", gif);
                        t.startActivity(intent);
                    }
                });


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
