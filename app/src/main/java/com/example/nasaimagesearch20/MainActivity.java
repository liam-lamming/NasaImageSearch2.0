package com.example.nasaimagesearch20;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends BaseActivity {

    private Button pickDateButton;
    private Button viewSavedImagesButton;
    private ProgressBar progressBar;
    private ListView listView;
    private ArrayList<NasaImage> nasaImages = new ArrayList<>();
    private NASAImageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up layout and toolbar
        setUpToolbar(R.layout.activity_main, true);

        // Initialize UI elements
        pickDateButton = findViewById(R.id.btnPickDate);
        viewSavedImagesButton = findViewById(R.id.btnViewSavedImages);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.listView);

        // Set up the adapter for ListView
        adapter = new NASAImageAdapter(this, nasaImages);
        listView.setAdapter(adapter);

        // Set the Button's click listener to trigger DatePickerDialog and search functionality
        pickDateButton.setOnClickListener(v -> showDatePickerDialog());

        // Handle item clicks in ListView to show detailed information
        listView.setOnItemClickListener((parent, view, position, id) -> {
            NasaImage selectedImage = nasaImages.get(position);
            displayNasaImage(selectedImage);
        });

        // Set click listener for the View Saved Images button
        viewSavedImagesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SavedImagesActivity.class);
            startActivity(intent);
        });
    }

    // Inflate the toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // Handle toolbar menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            // Show help fragment with specific information for MainActivity
            showHelpFragment("To search for a NASA image, select the date and hit OK! You can also view saved images by clicking on 'View Saved Images'.");
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

    // Method to show DatePickerDialog
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        // Create a DatePickerDialog with spinner mode for easier year and month selection
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    pickDateButton.setText(selectedDate);  // Set the selected date on the button
                    fetchNasaImage(selectedDate);  // Fetch the NASA image for the selected date
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set the start date of the picker to 1995-06-16, the earliest date available for the NASA API
        datePickerDialog.getDatePicker().setMinDate(new GregorianCalendar(1995, Calendar.JUNE, 16).getTimeInMillis());
        // Set the end date of the picker to today's date
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    // Method to initiate fetching the NASA image
    private void fetchNasaImage(String date) {
        new FetchImageTask().execute(date);
    }

    // AsyncTask to fetch the NASA image in the background
    private class FetchImageTask extends AsyncTask<String, Void, NasaImage> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
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
            progressBar.setVisibility(View.GONE);

            if (result != null) {
                nasaImages.add(result);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Image fetched successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(R.id.listView), "Error fetching data", Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> fetchNasaImage(pickDateButton.getText().toString()))
                        .show();
            }
        }
    }

    // Method to display the selected NASA image details in a new activity
    private void displayNasaImage(NasaImage image) {
        Intent intent = new Intent(MainActivity.this, ImageDetailActivity.class);
        intent.putExtra("image_url", image.getUrl());
        intent.putExtra("image_hdurl", image.getHdurl());
        intent.putExtra("image_title", image.getTitle());
        intent.putExtra("image_date", image.getDate());
        intent.putExtra("image_explanation", image.getExplanation());
        intent.putExtra("image_copyright", image.getCopyright());
        startActivity(intent);
    }
}
