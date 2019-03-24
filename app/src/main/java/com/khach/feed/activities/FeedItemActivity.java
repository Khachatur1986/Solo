package com.khach.feed.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.khach.feed.R;
import com.khach.feed.database.TableControllerFeed;
import com.khach.feed.models.PinnedModel;

public class FeedItemActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextView tvFeedDescript;
    private CollapsingToolbarLayout toolbarLayout;
    private ImageView ivFeedImg;

    private TableControllerFeed tableControllerFeed;

    private String webTitle;
    private String sectionName;
    private String thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_item);

        initViews();
        setSupportActionBar(toolbar);
        fab.setOnClickListener(this);

        if (getIntent() != null) {
            webTitle = getIntent().getStringExtra("webTitle");
            sectionName = getIntent().getStringExtra("sectionName");
            thumbnail = getIntent().getStringExtra("thumbnail");

            tvFeedDescript.setText(webTitle);
            toolbarLayout.setTitle(sectionName);
            toolbarLayout.setBackgroundResource(R.drawable.ic_launcher_background);
            Glide.with(getApplicationContext()).asDrawable().load(thumbnail).into(ivFeedImg);
        }
        tableControllerFeed = new TableControllerFeed(this);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        tvFeedDescript = findViewById(R.id.tv_feed_descript);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        ivFeedImg = findViewById(R.id.iv_feed_img);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            if (webTitle != null && sectionName != null && thumbnail != null) {
                boolean isOk = tableControllerFeed.create(new PinnedModel(webTitle, sectionName, thumbnail));
                if (isOk) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("webTitle", webTitle);
                    returnIntent.putExtra("sectionName", sectionName);
                    returnIntent.putExtra("thumbnail", thumbnail);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }

        }
    }
}
