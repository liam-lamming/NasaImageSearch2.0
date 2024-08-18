package com.example.nasaimagesearch20;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleTextView;
    private TextView dateTextView;
    private Button backButton;
    private Button saveButton;

    private String imageUrl;
    private String imageTitle;
    private String imageDate;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        // Initialize UI elements
        imageView = findViewById(R.id.imageView);
        titleTextView = findViewById(R.id.titleTextView);
        dateTextView = findViewById(R.id.dateTextView);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);

        // Retrieve data from the intent
        imageUrl = getIntent().getStringExtra("image_url");
        imageTitle = getIntent().getStringExtra("image_title");
        imageDate = getIntent().getStringExtra("image_date");

        // Set the title and date text
        titleTextView.setText(imageTitle);
        dateTextView.setText(imageDate);

        // Load the image using Glide
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource); // Display the image
                        imageBitmap = resource; // Store the bitmap for saving later
                    }

                    @Override
                    public void onLoadCleared(@Nullable android.graphics.drawable.Drawable placeholder) {
                        // This is called when the image view is cleared, handle any cleanup here if necessary
                    }
                });

        // Back Button: Navigate back to the MainActivity
        backButton.setOnClickListener(v -> finish());

        // Save Button: Save the image to the device
        saveButton.setOnClickListener(v -> saveImageToDevice());
    }

    // Save the image to the device storage
    private void saveImageToDevice() {
        if (imageBitmap != null) {
            try {
                String filename = imageTitle + "_" + imageDate + ".png";
                FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();

                Toast.makeText(this, "Image saved!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save image.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Image not loaded yet.", Toast.LENGTH_SHORT).show();
        }
    }
}
