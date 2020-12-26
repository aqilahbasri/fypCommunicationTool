package com.example.fypcommunicationtool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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


import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class Practicee extends AppCompatActivity {

    Button choose;
    ImageView imageView;
    TextView result;
    ImageLabeler labeler;
    private ActionBar toolbar;
    public Uri imguri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        assert extras != null;
        String data = extras.getString("catTitle");

//        toolbar = getSupportActionBar();
//        assert toolbar != null;
//        toolbar.setTitle(data);

        choose = findViewById(R.id.choose);
        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);

        AutoMLImageLabelerLocalModel localModel =
                new AutoMLImageLabelerLocalModel.Builder()
                        .setAssetFilePath("manifest.json")
                        // or .setAbsoluteFilePath(absolute file path to manifest file)
                        .build();

        AutoMLImageLabelerOptions autoMLImageLabelerOptions =
                new AutoMLImageLabelerOptions.Builder(localModel)
                        .setConfidenceThreshold(0.0f)  // Evaluate your model in the Firebase console
                        // to determine an appropriate value.
                        .build();
        ImageLabeler labeler = ImageLabeling.getClient(autoMLImageLabelerOptions);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
               // intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });


        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null  && data.getData() != null ){
            imguri = data.getData();
            imageView.setImageURI(imguri);

           InputImage image;
            try {
                image = InputImage.fromFilePath(getApplicationContext(), Objects.requireNonNull(data.getData()));

                labeler.process(image)
                        .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                            @Override
                            public void onSuccess(List<ImageLabel> labels) {
                                // Task completed successfully
                                // ...
                                String text = labels.get(0).getText().toUpperCase();
                                float confidence = labels.get(0).getConfidence();
                                // int index = label.getIndex();
                                result.setText("You did sign language of "+text+ " -  "+String.format("%.2f", confidence*100)+"%"+ "\n");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                // ...
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


}
