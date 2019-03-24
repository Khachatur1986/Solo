package com.khach.feed.activities.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        public PinnedViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {

        }
    }
}
