package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fypcommunicationtool.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddGIFActivity extends AppCompatActivity {

    private String messageReceiverID, messageReceiverName, messageReceiverImage, messageSenderID, imageUrl;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private StorageReference PendingGIFImagesRef;
    private StorageTask uploadTask;

    private ImageButton backButton, cropButton, stickerButton, textButton, editButton, imageButton;
    private CircleImageView imageProfile;
    private WebView capturedImage;
    private EditText malayCaption, engCaption;
    private FloatingActionButton sendButton;

    private Uri imageUri;
    private String saveCurrentTime, saveCurrentDate, imageId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gif);

        messageReceiverID = getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName = getIntent().getExtras().get("visit_user_name").toString();
        messageReceiverImage = getIntent().getExtras().get("visit_image").toString();
        imageUrl = getIntent().getExtras().get("imageUrl").toString();

        mAuth = FirebaseAuth.getInstance();
//        mAuth = FirebaseAuth.getInstance();
        messageSenderID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
        PendingGIFImagesRef = FirebaseStorage.getInstance().getReference().child("Pending GIF");

        IntializeControllers();

        imageUri = ChatsPrivateActivity.imageUri;
        imageId = ChatsPrivateActivity.imageID;

        capturedImage.loadUrl(imageUrl);
        capturedImage.getSettings().setLoadWithOverviewMode(true);
        capturedImage.getSettings().setUseWideViewPort(true);
//        capturedImage.setImageURI(imageUri);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitToAdmin();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(imageId);

                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        Toast.makeText(com.example.fypcommunicationtool.AddGIFActivity.this, "Message Sent Successfully...", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                        Toast.makeText(com.example.fypcommunicationtool.AddGIFActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }

    @SuppressLint("WrongViewCast")
    private void IntializeControllers() {
        backButton = (ImageButton) findViewById(R.id.btn_back);
        cropButton = (ImageButton) findViewById(R.id.btn_crop);
        capturedImage = (WebView) findViewById(R.id.image_view);
        malayCaption = (EditText) findViewById(R.id.addMalayCaption);
        engCaption = (EditText) findViewById(R.id.addEnglishCaption);
        sendButton = (FloatingActionButton) findViewById(R.id.btn_send);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());
    }

    private void submitToAdmin() {
        String inputEngCaption = engCaption.getText().toString();
        String inputMalayCaption = malayCaption.getText().toString();


        Intent intent = getIntent();
        String messagePushID = intent.getStringExtra("messagePushID");
        HashMap<String, String> gifDetails = (HashMap<String, String>) intent.getSerializableExtra("gifDetails");

        gifDetails.put("engCaption", inputEngCaption);
        gifDetails.put("malayCaption", inputMalayCaption);
        gifDetails.put("time", saveCurrentTime);
        gifDetails.put("date", saveCurrentDate);

        RootRef.child("PendingGIF").child(messageSenderID).child(messagePushID).setValue(gifDetails)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if (task.isSuccessful())
                        {
//                            loadingBar.dismiss();
                            Toast.makeText(com.example.fypcommunicationtool.AddGIFActivity.this, "Image uploaded", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
//                            loadingBar.dismiss();
                            Toast.makeText(com.example.fypcommunicationtool.AddGIFActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
//                        MessageInputText.setText("");
                    }
                });
        SendUserToPrivateChatActivity();
    }

    private void SendUserToPrivateChatActivity() {
        Intent intent = new Intent(com.example.fypcommunicationtool.AddGIFActivity.this, ChatsPrivateActivity.class);
        intent.putExtra("visit_user_id", messageReceiverID);
        intent.putExtra("visit_user_name", messageReceiverName);
        intent.putExtra("visit_image", messageReceiverImage);
        startActivity(intent);
    }
}