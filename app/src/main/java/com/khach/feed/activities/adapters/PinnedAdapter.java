package com.khach.feed.activities.adapters;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.khach.feed.models.PinnedModel;

import java.util.ArrayList;
import java.util.List;

public class PinnedAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private ArrayList<PinnedModel> pinnedModels;

    public PinnedAdapter(ArrayList<PinnedModel> pinnedModels) {
        this.pinnedModels = pinnedModels;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PinnedViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.row_item_home, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        baseViewHolder.onBind(i);
    }

    @Override
    public int getItemCount() {
        return pinnedModels == null ? 0 : pinnedModels.size();
    }

    public void add(PinnedModel pinnedModel) {
        pinnedModels.add(pinnedModel);
        notifyItemInserted(pinnedModels.size() - 1);
    }

    public void remove(PinnedModel item) {
        int position = pinnedModels.indexOf(item);
        if (position > -1) {
            pinnedModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public PinnedModel getItem(int position) {
        return pinnedModels.get(position);
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public void addAll(List<PinnedModel> models) {
        for (PinnedModel p : models) {
            add(p);
        }
    }

    class PinnedViewHolder extends BaseViewHolder {
        final CardView cvFeedRowItem;
        final ProgressBar pbHomeLoadingProgress;
        final ImageView ivFeedThumbnail;
        final TextView tvFeedSectionName;
        final TextView tvFeedWebTitle;

        public PinnedViewHolder(View itemView) {
            super(itemView);
            cvFeedRowItem = itemView.findViewById(R.id.cv_home_row_item);
            pbHomeLoadingProgress = itemView.findViewById(R.id.pb_home_loading_progress);
            ivFeedThumbnail = itemView.findViewById(R.id.iv_home_thumbnail);
            tvFeedSectionName = itemView.findViewById(R.id.tv_home_section_name);
            tvFeedWebTitle = itemView.findViewById(R.id.tv_home_web_title);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            cvFeedRowItem.setTag(position);
            cvFeedRowItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "test", Snackbar.LENGTH_SHORT).show();
                }
            });
            tvFeedSectionName.setText(pinnedModels.get(position).getSectionName());
            tvFeedWebTitle.setText(pinnedModels.get(position).getWebTitle());
            Glide.with(ivFeedThumbnail)
                    .load(pinnedModels.get(position).getThumbnail())
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
}
