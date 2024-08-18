package com.example.nasaimagesearch20;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button pickDateButton;
    private ProgressBar progressBar;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickDateButton = findViewById(R.id.btnPickDate);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.listView);

        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        fetchNasaImage(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void fetchNasaImage(String date) {
        new FetchImageTask().execute(date);
    }

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
                displayNasaImage(result);
            } else {
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayNasaImage(NasaImage image) {
        Intent intent = new Intent(MainActivity.this, ImageDetailActivity.class);
        intent.putExtra("image_url", image.url);
        intent.putExtra("image_hdurl", image.hdurl);
        intent.putExtra("image_title", image.title);
        intent.putExtra("image_date", image.date);
        startActivity(intent);
    }
}
