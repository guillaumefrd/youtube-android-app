package com.example.guill.youtube;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoViewHolder>{
    private final List<Item> videos;

    public VideoRecyclerAdapter(List<Item> videos) {
        this.videos = videos;
    }

    public List<Item> getVideos() {
        return videos;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_video, parent, false));
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.bind(videos.get(position));
    }

    @Override
    public int getItemCount() {
        return videos != null ? videos.size() : 0;
    }
}
