package com.khach.feed.activities.adapters;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.khach.feed.R;
import com.khach.feed.activities.view_holders.BaseViewHolder;
import com.khach.feed.pojo.Result;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private ArrayList<Result> feedModelArrayList;

    private FeedItemClickListener feedItemClickListener;

    public HomeAdapter(ArrayList<Result> feedModelArrayList, FeedItemClickListener feedItemClickListener) {
        this.feedModelArrayList = feedModelArrayList;
        this.feedItemClickListener = feedItemClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case VIEW_TYPE_NORMAL:
                return new FeedViewHolder(
                        LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.row_item_home, viewGroup, false));
            case VIEW_TYPE_LOADING:
                return new FooterHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_loading, viewGroup, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder feedViewHolder, final int i) {
        feedViewHolder.onBind(i);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == feedModelArrayList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return feedModelArrayList == null ? 0 : feedModelArrayList.size();
    }

    public void add(Result response) {
        feedModelArrayList.add(response);
        notifyItemInserted(feedModelArrayList.size() - 1);
    }

    public void addAll(List<Result> results) {
        for (Result response : results) {
            add(response);
        }
    }

    private void remove(Result item) {
        int position = feedModelArrayList.indexOf(item);
        if (position > -1) {
            feedModelArrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new Result());
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = feedModelArrayList.size() - 1;
        Result item = getItem(position);
        if (item != null) {
            feedModelArrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    Result getItem(int position) {
        return feedModelArrayList.get(position);
    }

    public class FeedViewHolder extends BaseViewHolder {
        final CardView cvFeedRowItem;
        final ProgressBar pbHomeLoadingProgress;
        final ImageView ivFeedThumbnail;
        final TextView tvFeedSectionName;
        final TextView tvFeedWebTitle;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            cvFeedRowItem = itemView.findViewById(R.id.cv_home_row_item);
            pbHomeLoadingProgress = itemView.findViewById(R.id.pb_home_loading_progress);
            ivFeedThumbnail = itemView.findViewById(R.id.iv_home_thumbnail);
            tvFeedSectionName = itemView.findViewById(R.id.tv_home_section_name);
            tvFeedWebTitle = itemView.findViewById(R.id.tv_home_web_title);
        }

        protected void clear() {

        }

        public void onBind(final int position) {
            super.onBind(position);
            final Result item = feedModelArrayList.get(position);

            cvFeedRowItem.setTag(position);
            cvFeedRowItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedItemClickListener.onItemClicked(
                            item.getWebTitle(),
                            item.getSectionName(),
                            item.getFields().getThumbnail()
                    );
                }
            });
            tvFeedSectionName.setText(feedModelArrayList.get(position).getSectionName());
            tvFeedWebTitle.setText(feedModelArrayList.get(position).getWebTitle());
            Glide.with(ivFeedThumbnail)
                    .load(item.getFields().getThumbnail())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            pbHomeLoadingProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target, DataSource dataSource,
                                                       boolean isFirstResource) {
                            pbHomeLoadingProgress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(ivFeedThumbnail);
        }
    }

    public class FooterHolder extends BaseViewHolder {
        ProgressBar mProgressBar;

        FooterHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }

        @Override
        protected void clear() {

        }

    }

    public interface FeedItemClickListener {
        void onItemClicked(String webTitle, String sectionName, String image);
    }

}
