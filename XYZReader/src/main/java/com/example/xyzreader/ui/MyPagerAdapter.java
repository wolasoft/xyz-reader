package com.example.xyzreader.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.xyzreader.data.entities.Article;

import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    List<Article> articles;
    MyPagerAdapter(FragmentManager fm, List<Article> articles) {
        super(fm);
        this.articles = articles;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return ArticleDetailFragment.newInstance(articles.get(position));
    }

    @Override
    public int getCount() {
        return (articles != null) ? articles.size() : 0;
    }
}
