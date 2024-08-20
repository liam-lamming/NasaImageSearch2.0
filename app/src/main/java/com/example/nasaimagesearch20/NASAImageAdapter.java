package com.example.nasaimagesearch20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NASAImageAdapter extends RecyclerView.Adapter<NASAImageAdapter.ImageViewHolder> {

    private Context context;
    private List<NasaImage> images;
    private OnItemClickListener onItemClickListener;

    // Constructor to initialize the adapter with context and the list of NASA images
    public NASAImageAdapter(Context context, List<NasaImage> images) {
        this.context = context;
        this.images = images;
    }

    // Interface to handle item clicks
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Method to set the item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_image, parent, false);
        return new ImageViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        // Get the current NASA image item
        NasaImage currentImage = images.get(position);

        // Set the title and image URL for each item in the RecyclerView
        holder.titleTextView.setText(currentImage.getTitle());

        Glide.with(context)
                .load(currentImage.getUrl()) // Load the image using Glide
                .into(holder.imageView); // Set the image in the ImageView
    }

    @Override
    public int getItemCount() {
        // Return the size of the image list
        return images != null ? images.size() : 0;
    }

    // Method to update the dataset and notify the adapter of changes
    public void updateImages(List<NasaImage> newImages) {
        this.images = newImages;
        notifyDataSetChanged();
    }

    // Inner class to represent the custom view for each item in the RecyclerView
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;

        public ImageViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            // Initialize the views for the title and image
            titleTextView = itemView.findViewById(R.id.titleTextView);
            imageView = itemView.findViewById(R.id.imageView);

            // Set up the click listener for the item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
