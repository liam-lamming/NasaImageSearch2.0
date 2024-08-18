package com.example.nasaimagesearch20;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ImageView imageView = findViewById(R.id.imageView);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);

        // Get data from Intent
        String imageUrl = getIntent().getStringExtra("image_url");
        String imageTitle = getIntent().getStringExtra("image_title");
        String imageDate = getIntent().getStringExtra("image_date");

        // Load image using Picasso
        Picasso.get().load(imageUrl).into(imageView);

        // Set text
        titleTextView.setText(imageTitle);
        dateTextView.setText(imageDate);
    }
}
