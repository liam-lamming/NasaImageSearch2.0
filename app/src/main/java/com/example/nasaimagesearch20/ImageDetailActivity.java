package com.example.nasaimagesearch20;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDetailActivity extends BaseActivity {

    private ImageView imageView;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView explanationTextView;
    private TextView copyrightTextView;
    private Button saveButton;

    private String imageUrl;
    private String imageHdUrl;
    private String imageTitle;
    private String imageDate;
    private String imageExplanation;
    private String imageCopyright;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the toolbar using BaseActivity's method
        setUpToolbar(R.layout.activity_image_detail, true); // Passing true to show the back button

        // Initialize UI elements
        imageView = findViewById(R.id.imageView);
        titleTextView = findViewById(R.id.titleTextView);
        dateTextView = findViewById(R.id.dateTextView);
        explanationTextView = findViewById(R.id.explanationTextView);
        copyrightTextView = findViewById(R.id.copyrightTextView);
        saveButton = findViewById(R.id.saveButton);

        // Retrieve data from the intent
        imageUrl = getIntent().getStringExtra("image_url");
        imageTitle = getIntent().getStringExtra("image_title");
        imageDate = getIntent().getStringExtra("image_date");
        imageExplanation = getIntent().getStringExtra("image_explanation");
        imageCopyright = getIntent().getStringExtra("image_copyright");

        // Set the title, date, and explanation text
        titleTextView.setText(imageTitle);
        dateTextView.setText(imageDate);
        explanationTextView.setText(imageExplanation != null ? imageExplanation : "Explanation not available");
        copyrightTextView.setText(imageCopyright != null ? "© " + imageCopyright : "Public Domain");

        // Load the image using Glide
        loadImage();

        // Save Button: Save the image to the device
        saveButton.setOnClickListener(v -> saveImageToDevice());
    }

    // Method to load the image using Glide
    private void loadImage() {
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
                        // Cleanup when image view is cleared
                    }

                    @Override
                    public void onLoadFailed(@Nullable android.graphics.drawable.Drawable errorDrawable) {
                        // Handle image load failure
                        Toast.makeText(ImageDetailActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Save the image to the device storage
    private void saveImageToDevice() {
        if (imageBitmap != null) {
            try {
                // Sanitize and create a unique filename based on the title and date
                String filename = sanitizeFilename(imageTitle + "_" + imageDate + ".png");
                FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();

                Toast.makeText(this, "Image saved as " + filename, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save image.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Image not loaded yet.", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to sanitize filenames by removing invalid characters
    private String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    // Method to display the help fragment with detailed instructions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            // Show the help fragment specific to ImageDetailActivity - App crashing when string enabled - unsure why
            showHelpFragment("This page displays information about your selected NASA image " +
                    "You can save the image to your device by clicking the 'Save' button.");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to show the help fragment
    private void showHelpFragment(String helpText) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        HelpFragment helpFragment = HelpFragment.newInstance(helpText);
        helpFragment.show(fragmentManager, "HelpFragment");
    }
}
