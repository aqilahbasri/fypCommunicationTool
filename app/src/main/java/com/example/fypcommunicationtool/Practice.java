package com.example.fypcommunicationtool;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerLocalModel;
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerOptions;

import java.util.List;

public class Practice extends AppCompatActivity {

    Button choose;
    ImageView imageView;
    TextView result;
    ImageLabeler labeler;
    private Toolbar toolbar;
    public Uri imguri;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

       Intent intent = getIntent();
       Bundle extras = intent.getExtras();
       String data = extras.getString("catTitle");

       toolbar = (Toolbar) findViewById(R.id.main_learning_toolbar);
       setSupportActionBar(toolbar);
       getSupportActionBar().setTitle(data);

        choose = findViewById(R.id.choose);
        imageView = findViewById(R.id.imageView);
        result = findViewById(R.id.result);

       AutoMLImageLabelerLocalModel localModel =
               new AutoMLImageLabelerLocalModel.Builder()
                       .setAssetFilePath("model/manifest.json")
                       // or .setAbsoluteFilePath(absolute file path to manifest file)
                       .build();


       AutoMLImageLabelerOptions autoMLImageLabelerOptions =
               new AutoMLImageLabelerOptions.Builder(localModel)
                       .setConfidenceThreshold(0.0f)  // Evaluate your model in the Firebase console
                       // to determine an appropriate value.
                       .build();
       labeler = ImageLabeling.getClient(autoMLImageLabelerOptions);



       choose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//               i.setType("image/*");
//               i.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(i,0);
           }
       });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // if(requestCode == 0 && resultCode == RESULT_OK && data != null && data.getData() != null){
//            imguri = data.getData();
//            imageView.setImageURI(imguri);
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);

            InputImage image;
//            try {
               // image = InputImage.fromFilePath(getApplicationContext(), Objects.requireNonNull(data.getData()));
                image = InputImage.fromBitmap(bitmap, 0);
                labeler.process(image)
                        .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                            @Override
                            public void onSuccess(List<ImageLabel> labels) {
                                // Task completed successfully
                                // ...
                               // for (ImageLabel label : labels) {
                                    String text = labels.get(0).getText().toUpperCase();
                                    float confidence = labels.get(0).getConfidence();
                                   // int index = label.getIndex();
                                    result.setText("You did sign language of "+text+ " -  "+String.format("%.2f", confidence*100)+"%"+ "\n");

                                //}
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                // ...
                            }
                        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Category.class);
        startActivity(intent);
    }
}

