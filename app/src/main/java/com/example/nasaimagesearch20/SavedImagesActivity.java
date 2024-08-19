package com.example.nasaimagesearch20;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SavedImagesActivity extends AppCompatActivity {

    private GridView savedImagesGridView;
    private List<File> savedImageFiles;
    private SavedImagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);

        savedImagesGridView = findViewById(R.id.savedImagesGridView);

        // Load saved images from the device
        loadSavedImages();

        // Set up the GridView with the saved image files
        adapter = new SavedImagesAdapter(this, savedImageFiles);
        savedImagesGridView.setAdapter(adapter);

        // Set up click listener for viewing saved images
        savedImagesGridView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle the viewing of the saved image
            File selectedImageFile = savedImageFiles.get(position);
            Intent intent = new Intent(SavedImagesActivity.this, ImageDetailActivity.class);
            intent.putExtra("image_path", selectedImageFile.getAbsolutePath());
            startActivity(intent);
        });

        // Long-click listener for deleting saved images
        savedImagesGridView.setOnItemLongClickListener((parent, view, position, id) -> {
            // Confirm before deleting the selected image
            new AlertDialog.Builder(SavedImagesActivity.this)
                    .setTitle("Delete Image")
                    .setMessage("Are you sure you want to delete this image?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Delete the selected image
                        deleteImage(position);
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        });
    }

    // Method to load saved images from the device's internal storage
    private void loadSavedImages() {
        File filesDir = getFilesDir();
        savedImageFiles = new ArrayList<>();

        // Iterate through the files in the directory and add image files to the list
        for (File file : filesDir.listFiles()) {
            if (file.getName().endsWith(".png")) {
                savedImageFiles.add(file);
            }
        }

        if (savedImageFiles.isEmpty()) {
            Toast.makeText(this, "No saved images", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to delete an image from the device and update the GridView
    private void deleteImage(int position) {
        File file = savedImageFiles.get(position);
        if (file.exists()) {
            if (file.delete()) {
                savedImageFiles.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
