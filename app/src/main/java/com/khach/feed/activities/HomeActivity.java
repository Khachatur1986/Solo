package com.khach.feed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.khach.feed.R;
import com.khach.feed.activities.adapters.HomeAdapter;
import com.khach.feed.activities.adapters.PinnedAdapter;
import com.khach.feed.activities.listeners.PaginationScrollListener;
import com.khach.feed.client.ApiClient;
import com.khach.feed.client.IFeedService;
import com.khach.feed.database.TableControllerFeed;
import com.khach.feed.models.PinnedModel;
import com.khach.feed.pojo.Feed;
import com.khach.feed.pojo.Result;
import com.khach.feed.services.FeedService;
import com.khach.feed.util.PreferencesUtil;
import com.khach.feed.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.khach.feed.util.Constants.API_KEY;
import static com.khach.feed.util.Constants.FEED_PREF_NAME;
import static com.khach.feed.util.Constants.FEED_PREF_TOTAL;
import static com.khach.feed.util.Constants.FORMAT;
import static com.khach.feed.util.Constants.Q;
import static com.khach.feed.util.Constants.SHOW_ELEMENTS;
import static com.khach.feed.util.Constants.SHOW_FIELDS;

public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        HomeAdapter.FeedItemClickListener {
    private boolean isServerOk;

    public static final int FEED_REQUEST_CODE = 11;
    public static final int PAGE_START = 1;
    private IFeedService iFeedService;
    private RecyclerView rvHomePinnedArticles;
    private PinnedAdapter pinnedAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvHomeFeeds;
    private HomeAdapter feedAdapter;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;
    private int totalPage = 10;

    private TableControllerFeed tableControllerFeed;
    private int limit = 10;
    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        tableControllerFeed = new TableControllerFeed(this);

        swipeRefresh.setOnRefreshListener(this);
        feedAdapter = new HomeAdapter(new ArrayList<Result>(), this);
        pinnedAdapter = new PinnedAdapter(new ArrayList<PinnedModel>());
        iFeedService = ApiClient.getInstance(getCacheDir()).createService(IFeedService.class);
        initHomeFeedsRecycleView();

        initPinnedFeedsRecycleView();
    }

    private void initPinnedFeedsRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvHomePinnedArticles.setHasFixedSize(true);
        rvHomePinnedArticles.setLayoutManager(linearLayoutManager);
        rvHomePinnedArticles.setAdapter(pinnedAdapter);
        loadPinnedItems();
    }

    private void loadPinnedItems() {
        List<PinnedModel> results = tableControllerFeed.readByLimitAndOffset(limit, offset);
        pinnedAdapter.addAll(results);
    }

    private void initHomeFeedsRecycleView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvHomeFeeds.setHasFixedSize(true);
        rvHomeFeeds.setLayoutManager(layoutManager);
        rvHomeFeeds.setAdapter(feedAdapter);
        preparedListItem();

        rvHomeFeeds.addOnScrollListener(new PaginationScrollListener<GridLayoutManager>(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                preparedListItem();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void preparedListItem() {
        Call<Feed> feedCall = iFeedService.loadFeeds(Q, FORMAT, SHOW_FIELDS, SHOW_ELEMENTS, API_KEY);
        feedCall.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(@NonNull Call<Feed> call, @NonNull Response<Feed> response) {
                Feed body = response.body();
                if (body != null) {
                    isServerOk = true;
                    PreferencesUtil.saveIntData(HomeActivity.this,
                            FEED_PREF_NAME, FEED_PREF_TOTAL, body.getResponse().getTotal());
                    List<Result> results = body.getResponse().getResults();
                    if (currentPage != PAGE_START) feedAdapter.removeLoading();
                    feedAdapter.addAll(results);
                    swipeRefresh.setRefreshing(false);
                    if (currentPage < totalPage) feedAdapter.addLoading();
                    else isLastPage = true;
                    isLoading = false;
                } else {
                    ViewUtil.showMessageDialog(HomeActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Feed> call, @NonNull Throwable t) {
                ViewUtil.showMessageDialog(HomeActivity.this, "Cant load feeds");
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        stopService(new Intent(this, FeedService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isServerOk) {
            startService(new Intent(this, FeedService.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServerOk) {
            startService(new Intent(this, FeedService.class));
        }
    }

    private void initViews() {
        rvHomePinnedArticles = findViewById(R.id.rv_home_pinned_articles);
        rvHomeFeeds = findViewById(R.id.rv_home_feeds);
        swipeRefresh = findViewById(R.id.swipeRefresh);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case FEED_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    PinnedModel pinnedModel = new PinnedModel(getIntent().getStringExtra("webTitle"),
                            getIntent().getStringExtra("sectionName"),
                            getIntent().getStringExtra("thumbnail"));
                    pinnedAdapter.add(pinnedModel);
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        currentPage = PAGE_START;
        isLastPage = false;
        feedAdapter.clear();
        preparedListItem();
    }

    @Override
    public void onItemClicked(String webTitle, String sectionName, String image) {
        Intent intent = new Intent(this, FeedItemActivity.class);
        intent.putExtra("webTitle", webTitle);
        intent.putExtra("sectionName", sectionName);
        intent.putExtra("thumbnail", image);
        startActivityForResult(intent, FEED_REQUEST_CODE);
    }
}
