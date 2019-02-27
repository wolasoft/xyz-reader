package com.example.xyzreader.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.xyzreader.R;
import com.example.xyzreader.data.entities.Article;
import com.example.xyzreader.ui.viewmodel.ArticleViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity implements Adapter.OnArticleClickedListener {

    private static final String TAG = ArticleListActivity.class.toString();
    private RecyclerView mRecyclerView;
    private boolean isTablet;
    private boolean isLandscape;

    private ArrayList<Article> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        isTablet = getResources().getBoolean(R.bool.is_tablet);
        isLandscape = getResources().getBoolean(R.bool.is_landscape);

        Toolbar mToolbar = findViewById(R.id.toolbar);

        // Use default locale format
        // Most time functions can only handle 1902 - 2037
        ArticleViewModel viewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);

        mRecyclerView = findViewById(R.id.recycler_view);

        viewModel.getArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                initViews(articles);
            }
        });
    }

    private void initViews(List<Article> articles) {
        if (articles == null) {
            return;
        }

        this.articles = new ArrayList<>(articles);

        Adapter adapter = new Adapter(articles, this);
        mRecyclerView.setAdapter(adapter);
        if (!isTablet && !isLandscape) {
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setHasFixedSize(true);
        } else {
            int columnCount = getResources().getInteger(R.integer.list_column_count);
            StaggeredGridLayoutManager sglm =
                    new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(sglm);
        }
    }

    @Override
    public void articleClicked(Article article) {
        Intent articleActivity = new Intent(this, ArticleDetailActivity.class);
        articleActivity.putExtra(ArticleDetailActivity.SELECTED_ARTICLE, article);
        articleActivity.putParcelableArrayListExtra(ArticleDetailActivity.ARTICLE_LIST, this.articles);
        startActivity(articleActivity);
    }
}
