package com.applaudostudios.newsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.applaudostudios.newsapp.model.News;

public class DetailsActivity extends AppCompatActivity {
    private static final String EXTRA_DETAIL = "EXTRA_DETAIL";
    private News mNews;

    public static Intent putNews(Context context, News news) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_DETAIL, news);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mNews = getIntent().getParcelableExtra(EXTRA_DETAIL);

        DetailsFragment.newInstance(mNews);
    }
}
