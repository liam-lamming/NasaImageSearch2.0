package com.example.nasaimagesearch20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NASAImageAdapter extends BaseAdapter {

    private Context context;
    private List<NasaImage> images;

    public NASAImageAdapter(Context context, List<NasaImage> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_image, parent, false);
        }

        NasaImage currentImage = images.get(position);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        titleTextView.setText(currentImage.getTitle());

        // Load the image using Glide
        Glide.with(context)
                .load(currentImage.getUrl())
                .into(imageView);

        return convertView;
    }
}
