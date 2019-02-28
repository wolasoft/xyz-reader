package com.example.xyzreader.ui;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.entities.Article;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment {
    private static final String TAG = "ArticleDetailFragment";

    private Article selectedArticle;
    private View mRootView;
    private int mMutedColor = 0xFF333333;

    private ImageView mPhotoView;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    // Use default locale format
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    // Most time functions can only handle 1902 - 2037
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(Article article) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ArticleDetailActivity.SELECTED_ARTICLE, article);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            postponeEnterTransition();
        }

        if (getArguments().containsKey(ArticleDetailActivity.SELECTED_ARTICLE)) {
            this.selectedArticle = getArguments().getParcelable(ArticleDetailActivity.SELECTED_ARTICLE);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getActivity())
                    .inflateTransition(android.R.transition.move));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementReturnTransition(null);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
        // getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);

        mPhotoView = mRootView.findViewById(R.id.photo);

        mRootView.findViewById(R.id.share_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText("Some sample text")
                        .getIntent(), getString(R.string.action_share)));
            }
        });

        bindViews();
        return mRootView;
    }

    private Date parsePublishedDate() {
        try {
            String date = this.selectedArticle.getPublishedDate();
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }

    private void bindViews() {
        if (mRootView == null) {
            return;
        }

        TextView titleView = mRootView.findViewById(R.id.article_title);
        TextView bylineView = mRootView.findViewById(R.id.article_byline);
        bylineView.setMovementMethod(new LinkMovementMethod());
        TextView bodyView = mRootView.findViewById(R.id.article_body);


        bodyView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Rosario-Regular.ttf"));

        if (this.selectedArticle != null) {
            mRootView.setAlpha(0);
            mRootView.setVisibility(View.VISIBLE);
            mRootView.animate().alpha(1);
            titleView.setText(this.selectedArticle.getTitle());
            Date publishedDate = parsePublishedDate();
            if (!publishedDate.before(START_OF_EPOCH.getTime())) {
                bylineView.setText(Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                publishedDate.getTime(),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + " by <font color='#ffffff'>"
                                + this.selectedArticle.getAuthor()
                                + "</font>"));

            } else {
                // If date is before 1902, just show the string
                bylineView.setText(Html.fromHtml(
                        outputFormat.format(publishedDate) + " by <font color='#ffffff'>"
                        + this.selectedArticle.getAuthor()
                                + "</font>"));

            }
            bodyView.setText(Html.fromHtml(this.selectedArticle.getBody().replaceAll("(\r\n|\n)", "<br />")));
            Picasso.get()
                    .load(this.selectedArticle.getPhoto())
                    .noFade()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            if (bitmap != null) {
                                Palette p = Palette.generate(bitmap, 12);
                                mMutedColor = p.getDarkMutedColor(0xFF333333);
                                mPhotoView.setImageBitmap(bitmap);
                                mRootView.findViewById(R.id.meta_bar)
                                        .setBackgroundColor(mMutedColor);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    startPostponedEnterTransition();
                                }
                            }
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startPostponedEnterTransition();
                            }
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            // do nothing
                        }
                    });
        } else {
            mRootView.setVisibility(View.GONE);
            titleView.setText("N/A");
            bylineView.setText("N/A" );
            bodyView.setText("N/A");
        }
    }
}
