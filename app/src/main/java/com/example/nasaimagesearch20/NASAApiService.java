package com.example.nasaimagesearch20;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NASAApiService {
    @GET("search")
    Call<NASAImageResponse> searchImages(@Query("q") String query);
}
