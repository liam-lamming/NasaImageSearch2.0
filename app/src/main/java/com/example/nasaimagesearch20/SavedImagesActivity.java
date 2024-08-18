package com.example.nasaimagesearch20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SavedImagesActivity extends AppCompatActivity {

    private ListView savedImagesListView;
    private List<String> savedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);

        savedImagesListView = findViewById(R.id.savedImagesListView);

        // Load saved images from the device
        loadSavedImages();

        // Set up the ListView with the saved images
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedImages);
        savedImagesListView.setAdapter(adapter);

        // Set up click listener for viewing saved images
        savedImagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle the viewing of the saved image
                String imageName = savedImages.get(position);
                // Load and display the image
                Toast.makeText(SavedImagesActivity.this, "Viewing: " + imageName, Toast.LENGTH_SHORT).show();
            }
        });

        // Long-click listener for deleting saved images
        savedImagesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Delete the selected image
                String imageName = savedImages.get(position);
                deleteImage(imageName);
                savedImages.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    // Method to load saved images from the device's internal storage
    private void loadSavedImages() {
        File filesDir = getFilesDir();
        savedImages = new ArrayList<>();

        for (File file : filesDir.listFiles()) {
            if (file.getName().endsWith(".png")) {
                savedImages.add(file.getName());
            }
        }

        if (savedImages.isEmpty()) {
            Toast.makeText(this, "No saved images", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to delete an image from the device
    private void deleteImage(String imageName) {
        File file = new File(getFilesDir(), imageName);
        if (file.exists()) {
            if (file.delete()) {
                Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
