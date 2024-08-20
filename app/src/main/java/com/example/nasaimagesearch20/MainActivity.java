package com.example.nasaimagesearch20;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private Button pickDateButton;
    private Button viewSavedImagesButton;
    private Button submitManualDateButton;
    private EditText editTextManualDate;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<NasaImage> nasaImages = new ArrayList<>();
    private NASAImageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up layout and toolbar
        setUpToolbar(R.layout.activity_main, false);

        // Initialize UI elements
        pickDateButton = findViewById(R.id.btnPickDate);
        viewSavedImagesButton = findViewById(R.id.btnViewSavedImages);
        editTextManualDate = findViewById(R.id.editTextManualDate);
        submitManualDateButton = findViewById(R.id.btnSubmitManualDate);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler_view);

        // Set up the adapter for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NASAImageAdapter(this, nasaImages);
        recyclerView.setAdapter(adapter);

        // Handle date picker button click
        pickDateButton.setOnClickListener(v -> showDatePickerDialog());

        // Handle RecyclerView item clicks to show detailed information
        adapter.setOnItemClickListener(position -> {
            NasaImage selectedImage = nasaImages.get(position);
            displayNasaImage(selectedImage);
        });

        // Set click listener for the View Saved Images button
        viewSavedImagesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SavedImagesActivity.class);
            startActivity(intent);
        });

        // Add TextWatcher for date formatting in yyyy-MM-dd format
        editTextManualDate.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String dateFormat = "yyyymmdd";
            private Calendar calendar = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanCurrent = current.replaceAll("[^\\d.]", "");

                    int sel = clean.length();
                    int selDiff = sel - cleanCurrent.length();

                    // Insert dashes automatically at the correct positions
                    if (clean.length() < 8) {
                        clean = clean + dateFormat.substring(clean.length());
                    } else {
                        // Format the date to yyyy-MM-dd
                        int year = Integer.parseInt(clean.substring(0, 4));
                        int month = Integer.parseInt(clean.substring(4, 6));
                        int day = Integer.parseInt(clean.substring(6, 8));

                        clean = String.format("%04d-%02d-%02d", year, month, day);
                    }

                    current = clean;
                    editTextManualDate.setText(current);
                    editTextManualDate.setSelection(Math.min(sel + selDiff, current.length()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Handle manual date submission via button click
        submitManualDateButton.setOnClickListener(v -> submitDate());

        // Handle manual date submission via keyboard "done" action
        editTextManualDate.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN) {
                submitDate();
                return true;
            }
            return false;
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
        if (item.getItemId() == R.id.action_help) {
            showHelpFragment("Select or enter a date to explore NASA images. You can also access your saved images by tapping 'Saved Images'");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to handle the date submission logic
    private void submitDate() {
        String manualDate = editTextManualDate.getText().toString().trim();
        if (isValidDate(manualDate)) {
            fetchNasaImage(manualDate);  // Fetch the NASA image
        } else {
            // Show error if the date is not valid and anchor the Snackbar to the EditText
            Snackbar.make(editTextManualDate, "Invalid date format. Please use yyyy-MM-dd.", Snackbar.LENGTH_LONG)
                    .setAnchorView(editTextManualDate)
                    .show();
        }
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

    // Method to validate date format
    private boolean isValidDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
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
            String apiKey = "pt7qvgXqwdDyKD1NdHFzFhdVyrI97R8FhWYIZyd9";
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
                Snackbar.make(recyclerView, "Error fetching data", Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> fetchNasaImage(pickDateButton.getText().toString()))
                        .show();
            }
        }
    }

    // Method to display the selected NASA image details in a new activity
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
