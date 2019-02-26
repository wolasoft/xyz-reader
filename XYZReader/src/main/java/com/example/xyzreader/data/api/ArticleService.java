package com.example.xyzreader.data.api;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ArticleService {
    @GET("data.json")
    Call<JsonArray> getAll();
}
