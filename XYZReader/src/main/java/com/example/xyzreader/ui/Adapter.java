package com.example.xyzreader.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.entities.Article;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private static final String TAG = "ADAPTER";
    private List<Article> articles;
    private final OnArticleClickedListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);
    private SimpleDateFormat outputFormat = new SimpleDateFormat();

    public Adapter(List<Article> articles, OnArticleClickedListener listener) {
        this.articles = articles;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_article, parent, false);

        return new ViewHolder(view);
    }

    private Date parsePublishedDate(String publishedDate) {
        try {
            return dateFormat.parse(publishedDate);
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = this.articles.get(position);
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        return this.articles == null ? 0 : this.articles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumbnailView;
        TextView titleView;
        TextView subtitleView;

        ViewHolder(View view) {
            super(view);
            thumbnailView = view.findViewById(R.id.thumbnail);
            titleView = view.findViewById(R.id.article_title);
            subtitleView = view.findViewById(R.id.article_subtitle);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            listener.articleClicked(position, thumbnailView);
        }

        private void bind(Article article) {
            titleView.setText(article.getTitle());
            Date publishedDate = parsePublishedDate(article.getPublishedDate());
            if (!publishedDate.before(START_OF_EPOCH.getTime())) {
                subtitleView.setText(Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                publishedDate.getTime(),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + ", By <b>"
                                + article.getAuthor() + "</b>"));
            } else {
                subtitleView.setText(Html.fromHtml(
                        outputFormat.format(publishedDate)
                                + ", By <b>"
                                + article.getAuthor() + "</b>"));
            }
            Picasso.get()
                    .load(article.getPhoto())
                    .into(thumbnailView);
        }
    }

    public interface OnArticleClickedListener {
        void articleClicked(int position, View sharedView);
    }
}
