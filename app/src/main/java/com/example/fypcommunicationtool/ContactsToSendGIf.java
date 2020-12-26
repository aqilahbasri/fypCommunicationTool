package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsToSendGIf extends AppCompatActivity {

    private View ContactsView;
    private RecyclerView myContactsList;
    private SearchView searchView;
    private FloatingActionButton addButton;

    private DatabaseReference ContactsRef, UsersRef,userMessageKeyRef, rootref;
    private FirebaseAuth mAuth;
    private String currentUserID;
    ArrayList<Contacts> list;
    String gifurl;

    String messageReceiverID, messageSenderID;
    private String saveCurrentTime, saveCurrentDate;
    String senderimage, sendername;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_to_send_g_if);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        gifurl = extras.getString("gifurl");


        myContactsList = (RecyclerView) findViewById(R.id.contacts_list);

        myContactsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        rootref = FirebaseDatabase.getInstance().getReference();;





        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());



        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(ContactsRef, Contacts.class).build();

        final FirebaseRecyclerAdapter<Contacts, ContactsViewHolder> adapter = new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ContactsViewHolder holder, int position, @NonNull Contacts model) {
                final String userIDs = getRef(position).getKey();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "You click " + userIDs, Toast.LENGTH_SHORT).show();
                        sendGif(userIDs);

                    }



                    private void sendGif(String userIDs) {

                     String userID = userIDs;
                     userMessageKeyRef = rootref.child("Messages").child(currentUserID).child(userID).push();
                    String messagePushID = userMessageKeyRef.getKey();
                        final String messageSenderRef = "Messages/" + currentUserID + "/" + userID;
                        final String messageReceiverRef = "Messages/" + userID + "/" + currentUserID;

                    Map messagePictureBody = new HashMap();
                    messagePictureBody.put("message", gifurl);
//                    messagePictureBody.put("name", fileUri.getLastPathSegment());
                    messagePictureBody.put("type", "image");
                    messagePictureBody.put("from", currentUserID);
                    messagePictureBody.put("to", userID);
                    messagePictureBody.put("messageID", messagePushID);
                    messagePictureBody.put("time", saveCurrentTime);
                    messagePictureBody.put("date", saveCurrentDate);

                        Map messageBodyDetails = new HashMap();
                        messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messagePictureBody);
                        messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messagePictureBody);


                    rootref.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "GIF sent successfully!" , Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(getApplicationContext() , ChatsPrivateActivity.class);
                                intent1.putExtra("visit_user_id", userIDs);
                                intent1.putExtra("visit_user_name", sendername);
                                intent1.putExtra("visit_image", senderimage);
                                startActivity(intent1);
                            }else{
                                Toast.makeText(getApplicationContext(), "GIF sending failed!" , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    rootref.child("Users").child(userIDs).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            sendername = snapshot.child("fullName").getValue().toString();
                            senderimage = snapshot.child("profileImage").getValue().toString();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    }

                });

                UsersRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
//                            list = new ArrayList<>();
//
//                            for(DataSnapshot ds : dataSnapshot.getChildren()){
//                                list.add(ds.getValue(Contacts.class));
//                            }

                            if (dataSnapshot.child("userState").hasChild("state"))
                            {
                                String state = dataSnapshot.child("userState").child("state").getValue().toString();
                                String date = dataSnapshot.child("userState").child("date").getValue().toString();
                                String time = dataSnapshot.child("userState").child("time").getValue().toString();

                                if (state.equals("online"))
                                {
                                    holder.onlineIcon.setVisibility(View.VISIBLE);
                                }
                                else if (state.equals("offline"))
                                {
                                    holder.onlineIcon.setVisibility(View.INVISIBLE);
                                }
                            }
                            else
                            {
                                holder.onlineIcon.setVisibility(View.INVISIBLE);
                            }

                            if (dataSnapshot.hasChild("profileImage"))
                            {
                                String userImage = dataSnapshot.child("profileImage").getValue().toString();
                                String profileName = dataSnapshot.child("userID").getValue().toString();
                                String profileFullName = dataSnapshot.child("fullName").getValue().toString();

                                holder.userName.setText(profileName);
                                holder.fullName.setText(profileFullName);
                                Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(holder.profileImage);
                            }
                            else
                            {
                                String profileName = dataSnapshot.child("userID").getValue().toString();
                                String profileFullName = dataSnapshot.child("fullName").getValue().toString();

                                holder.userName.setText(profileName);
                                holder.fullName.setText(profileFullName);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_user_display, viewGroup, false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;

            }
        };


        myContactsList.setAdapter(adapter);
        adapter.startListening();




    }


    public static class ContactsViewHolder extends RecyclerView.ViewHolder   {
        TextView userName, fullName;
        CircleImageView profileImage;
        ImageView onlineIcon;


        public ContactsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.user_profile_name);
            fullName = itemView.findViewById(R.id.user_fullName);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            onlineIcon = (ImageView) itemView.findViewById(R.id.user_online_status);
        }
    }
}