package com.example.nasaimagesearch20;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class SavedImagesAdapter extends BaseAdapter {

    private Context context;
    private List<File> imageFiles;

    // Constructor to initialize context and the list of image files
    public SavedImagesAdapter(Context context, List<File> imageFiles) {
        this.context = context;
        this.imageFiles = imageFiles;
    }

    // Return the total number of images
    @Override
    public int getCount() {
        return imageFiles.size();
    }

    // Return the image file at the specified position
    @Override
    public Object getItem(int position) {
        return imageFiles.get(position);
    }

    // Return the ID of the item at the specified position (usually position itself)
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Create a view for each image in the list and set the corresponding image using Glide
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout for each grid item if it's not already inflated
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_image, parent, false);
        }

        // Reference the ImageView in the layout
        ImageView imageView = convertView.findViewById(R.id.imageView);

        // Load the image using Glide
        Glide.with(context)
                .load(imageFiles.get(position))
                .into(imageView);

        return convertView;
    }
}
