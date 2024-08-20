package com.example.nasaimagesearch20;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SavedImagesActivity extends BaseActivity {

    private GridView savedImagesGridView;
    private List<File> savedImageFiles;
    private SavedImagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up layout and toolbar
        setUpToolbar(R.layout.activity_saved_images, true);

        // Initialize GridView for displaying saved images
        savedImagesGridView = findViewById(R.id.savedImagesGridView);

        // Load saved images from the device
        loadSavedImages();

        // Set up the GridView with the saved image files
        adapter = new SavedImagesAdapter(this, savedImageFiles);
        savedImagesGridView.setAdapter(adapter);

        // Set up click listener for viewing saved images
        savedImagesGridView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle the image enlargement by triggering a fragment
            File selectedImageFile = savedImageFiles.get(position);
            showImageInFragment(selectedImageFile);
        });

        // Long-click listener for deleting saved images
        savedImagesGridView.setOnItemLongClickListener((parent, view, position, id) -> {
            // Confirm before deleting the selected image
            new AlertDialog.Builder(SavedImagesActivity.this)
                    .setTitle(getString(R.string.delete_image_title))
                    .setMessage(getString(R.string.delete_image_message))
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
            Toast.makeText(this, getString(R.string.no_saved_images), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to delete an image from the device and update the GridView
    private void deleteImage(int position) {
        File file = savedImageFiles.get(position);
        if (file.exists()) {
            if (file.delete()) {
                savedImageFiles.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, getString(R.string.image_deleted), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.failed_delete_image), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to display the help fragment with detailed instructions for this page
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {

            String helpMessage= getString(R.string.saved_images_help);
            // Show the help fragment specific to SavedImagesActivity
            showHelpFragment(helpMessage);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to show the help fragment
    private void showHelpFragment(String helpText) {
        HelpFragment helpFragment = HelpFragment.newInstance(helpText);
        helpFragment.show(getSupportFragmentManager(), "HelpFragment");
    }

    // Method to display the selected image in a fragment for enlargement
    private void showImageInFragment(File imageFile) {
        // Create and show the fragment to enlarge the image
        FullscreenImageFragment fragment = FullscreenImageFragment.newInstance(imageFile.getAbsolutePath());
        fragment.show(getSupportFragmentManager(), "ImageViewerFragment");
    }
}
