package com.example.xyzreader.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.xyzreader.R;
import com.example.xyzreader.data.entities.Article;

import java.util.List;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    public static final String SELECTED_ARTICLE = "SELECTED_ARTICLE";
    public static final String ARTICLE_LIST = "ARTICLE_LIST";
    private int selectedArticlePosition;
    private List<Article> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra(SELECTED_ARTICLE)) {
            selectedArticlePosition = getIntent().getIntExtra(SELECTED_ARTICLE, 0);
        }

        if (getIntent().hasExtra(ARTICLE_LIST)) {
            this.articles = getIntent().getParcelableArrayListExtra(ARTICLE_LIST);
        }

        MyPagerAdapter mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), this.articles);
        ViewPager mPager = findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageMarginDrawable(new ColorDrawable(0x22000000));

        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

            @Override
            public void onPageSelected(int position) {
            }
        });

        if (this.articles != null) {
            mPager.setCurrentItem(selectedArticlePosition, false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
