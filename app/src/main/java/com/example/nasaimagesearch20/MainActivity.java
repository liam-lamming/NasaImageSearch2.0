package com.example.nasaimagesearch20;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends BaseActivity {

    private Button pickDateButton;
    private Button viewSavedImagesButton;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<NasaImage> nasaImages = new ArrayList<>();
    private NASAImageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for the activity and initialize the toolbar
        setUpToolbar(R.layout.activity_main, false);

        // Initialize UI elements
        pickDateButton = findViewById(R.id.btnPickDate);
        viewSavedImagesButton = findViewById(R.id.btnViewSavedImages);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler_view);

        // Set up the RecyclerView and Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NASAImageAdapter(this, nasaImages);
        recyclerView.setAdapter(adapter);

        // Set the Button's click listener to trigger DatePickerDialog and search functionality
        pickDateButton.setOnClickListener(v -> showDatePickerDialog());

        // Handle RecyclerView item clicks to show detailed information about the selected image
        adapter.setOnItemClickListener(position -> displayNasaImage(nasaImages.get(position)));

        // Set click listener for View Saved Images button
        viewSavedImagesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SavedImagesActivity.class);
            startActivity(intent);
        });
    }

    // Inflate the toolbar menu and set up the SearchView
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu); // Reference the correct menu resource

        // Configure the SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search by date");

        // Handle search query submission
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Trigger the NASA image search based on the date entered in the search view
                fetchNasaImage(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optionally handle real-time text changes
                return false;
            }
        });

        return true;
    }

    // Handle menu item selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                // Show help fragment for MainActivity
                showHelpFragment("Select a date to explore NASA images. You can also access your saved images by tapping 'Saved Images'");
                return true;
            case R.id.action_settings:
                // Handle settings if needed
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Display the help fragment with specific information for this activity
    private void showHelpFragment(String helpText) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        HelpFragment helpFragment = HelpFragment.newInstance(helpText);
        helpFragment.show(fragmentManager, "HelpFragment");
    }

    // Show the DatePickerDialog to allow the user to pick a date for searching NASA images
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        // Configure DatePickerDialog with the available date range
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    pickDateButton.setText(selectedDate);  // Display the selected date on the button
                    fetchNasaImage(selectedDate);  // Fetch the NASA image for the selected date
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum and maximum date limits
        datePickerDialog.getDatePicker().setMinDate(new GregorianCalendar(1995, Calendar.JUNE, 16).getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    // Fetch the NASA image for the selected date
    private void fetchNasaImage(String date) {
        new FetchImageTask().execute(date);
    }

    // AsyncTask to fetch the NASA image in the background
    private class FetchImageTask extends AsyncTask<String, Void, NasaImage> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);  // Show the progress bar while fetching the data
        }

        @Override
        protected NasaImage doInBackground(String... params) {
            String date = params[0];
            String apiKey = "pt7qvgXqwdDyKD1NdHFzFhdVyrI97R8FhWYIZyd9";  // NASA API key
            String apiUrl = "https://api.nasa.gov/planetary/apod?api_key=" + apiKey + "&date=" + date;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(apiUrl).build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    return gson.fromJson(response.body().string(), NasaImage.class);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(NasaImage result) {
            progressBar.setVisibility(View.GONE);  // Hide the progress bar when the task is complete

            if (result != null) {
                nasaImages.add(result);  // Add the fetched image to the list
                adapter.notifyDataSetChanged();  // Notify the adapter of the data change
                Toast.makeText(MainActivity.this, "Image fetched successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Snackbar.make(recyclerView, "Error fetching data", Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> fetchNasaImage(pickDateButton.getText().toString()))
                        .show();
            }
        }
    }

    // Display the selected NASA image details in a new activity
    private void displayNasaImage(NasaImage image) {
        Intent intent = new Intent(MainActivity.this, ImageDetailActivity.class);
        intent.putExtra("image_url", image.getUrl());
        intent.putExtra("image_title", image.getTitle());
        intent.putExtra("image_date", image.getDate());
        intent.putExtra("image_explanation", image.getExplanation());
        intent.putExtra("image_copyright", image.getCopyright());
        startActivity(intent);
    }
}
