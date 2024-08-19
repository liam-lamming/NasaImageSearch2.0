package com.example.nasaimagesearch20;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.github.chrisbanes.photoview.PhotoView;
import java.io.File;

public class FullscreenImageFragment extends DialogFragment {

    private static final String ARG_IMAGE_PATH = "image_path";

    // Factory method to create a new instance of the fragment with the image path as an argument
    public static FullscreenImageFragment newInstance(String imagePath) {
        FullscreenImageFragment fragment = new FullscreenImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_PATH, imagePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_image_fullscreen, container, false);

        // Get references to UI components
        PhotoView fullscreenImageView = view.findViewById(R.id.fullscreen_image);  // Using PhotoView for zoom and pan functionality
        Button closeButton = view.findViewById(R.id.close_button);

        // Get the image path from arguments
        String imagePath = getArguments().getString(ARG_IMAGE_PATH);

        // Load the image directly from the file
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            fullscreenImageView.setImageBitmap(bitmap); // Display the image in the ImageView
        }

        // Set up the close button
        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }
}
