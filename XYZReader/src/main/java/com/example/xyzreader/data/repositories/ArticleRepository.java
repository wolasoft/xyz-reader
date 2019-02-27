package com.example.xyzreader.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.xyzreader.data.api.ApiConnector;
import com.example.xyzreader.data.api.ArticleService;
import com.example.xyzreader.data.entities.Article;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleRepository {
    private static final String TAG = "ArticleRepository";
    private ArticleService service;
    private Gson gson;
    private static ArticleRepository instance = null;

    private ArticleRepository(ArticleService service) {
        this.service = service;
        this.gson = new Gson();
    }

    public static synchronized ArticleRepository getInstance() {
        if (instance == null)
            instance = new ArticleRepository(ApiConnector.createRetrofitService(ArticleService.class));
        return instance;
    }

    public LiveData<List<Article>> getAll() {
        final MutableLiveData<List<Article>> data = new MutableLiveData<>();
        Call call = service.getAll();
        Log.d(TAG, "Fetching recipes");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    Type listType = new TypeToken<ArrayList<Article>>(){}.getType();
                    List<Article> recipes = gson.fromJson(response.body().toString(), listType);
                    data.setValue(recipes);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
