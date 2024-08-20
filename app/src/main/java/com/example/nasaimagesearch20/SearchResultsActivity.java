package com.example.nasaimagesearch20;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://images-api.nasa.gov/";
    private NASAImageAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String query = getIntent().getStringExtra("SEARCH_QUERY");
        searchImages(query);
    }

    private void searchImages(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NASAApiService apiService = retrofit.create(NASAApiService.class);
        Call<NASAImageResponse> call = apiService.searchImages(query);

        call.enqueue(new Callback<NASAImageResponse>() {
            @Override
            public void onResponse(Call<NASAImageResponse> call, Response<NASAImageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NASAImageResponse nasaImageResponse = response.body();
                    List<NasaImage> nasaImages = convertToNasaImages(nasaImageResponse.getItems());
                    displayResults(nasaImages);
                } else {
                    Toast.makeText(SearchResultsActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NASAImageResponse> call, Throwable t) {
                Toast.makeText(SearchResultsActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                Log.e("API_CALL", "Error fetching images", t);
            }
        });
    }

    private List<NasaImage> convertToNasaImages(List<NASAImageResponse.Item> items) {
        List<NasaImage> nasaImages = new ArrayList<>();
        for (NASAImageResponse.Item item : items) {
            NasaImage image = new NasaImage();
            image.setTitle(item.getData().isEmpty() ? "" : item.getData().get(0).getTitle());
            image.setUrl(item.getLinks().isEmpty() ? "" : item.getLinks().get(0).getHref());
            // Set other fields as needed
            nasaImages.add(image);
        }
        return nasaImages;
    }

    private void displayResults(List<NasaImage> nasaImages) {
        adapter = new NASAImageAdapter(this, nasaImages);
        recyclerView.setAdapter(adapter);
    }
}
