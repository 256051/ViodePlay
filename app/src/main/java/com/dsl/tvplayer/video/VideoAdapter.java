package com.dsl.tvplayer.video;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.dsl.tvplayer.R;
import com.dsl.tvplayer.entity.VideoBean;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private List<VideoBean> videos;
    private Context context;

    public VideoAdapter(List<VideoBean> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_video, parent, false);
        return new VideoHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final VideoHolder holder, int position) {

        VideoBean videoBean = videos.get(position);
        Glide.with(context)
                .load(videoBean.getImage())
                .placeholder(android.R.color.white)
                .into(holder.thumb);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        private ImageView thumb;

        VideoHolder(View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
        }
    }
}