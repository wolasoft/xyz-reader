package com.example.xyzreader.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.example.xyzreader.R;
import com.example.xyzreader.data.entities.Article;

import java.util.List;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    public static final String SELECTED_ARTICLE = "SELECTED_ARTICLE";
    public static final String ARTICLE_LIST = "ARTICLE_LIST";
    private Article selectedArticle;
    private List<Article> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MyPagerAdapter mPagerAdapter = new MyPagerAdapter(getFragmentManager());
        ViewPager mPager = findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        //mPager.setPageMargin((int) TypedValue
          //      .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        mPager.setPageMarginDrawable(new ColorDrawable(0x22000000));

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

            @Override
            public void onPageSelected(int position) {
            }
        });

        if (getIntent().hasExtra(SELECTED_ARTICLE)) {
            if (getIntent().getParcelableExtra(SELECTED_ARTICLE) != null) {
                selectedArticle = getIntent().getParcelableExtra(SELECTED_ARTICLE);
            }
        }

        if (getIntent().hasExtra(ARTICLE_LIST)) {
            this.articles = getIntent().getParcelableArrayListExtra(ARTICLE_LIST);
        }

        if (selectedArticle != null && this.articles != null) {
            mPagerAdapter.notifyDataSetChanged();
            final int position = this.articles.indexOf(selectedArticle);
            mPager.setCurrentItem(position, false);
        }
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            // ArticleDetailFragment fragment = (ArticleDetailFragment) object;
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
}
