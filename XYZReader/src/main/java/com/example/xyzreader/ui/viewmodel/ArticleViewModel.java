package com.example.xyzreader.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.xyzreader.data.entities.Article;
import com.example.xyzreader.data.repositories.ArticleRepository;

import java.util.List;

public class ArticleViewModel extends AndroidViewModel {

    public ArticleRepository repository;

    public ArticleViewModel(Application application) {
        super(application);
        this.repository = ArticleRepository.getInstance();
    }

    public LiveData<List<Article>> getArticles() {
        return this.repository.getAll();
    }
}