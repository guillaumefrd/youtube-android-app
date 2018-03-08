package com.example.guill.youtube;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView channel;
    private ImageView imageView;

    public VideoViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        channel = (TextView) itemView.findViewById(R.id.channel);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }

    public void bind(Item item) {
        title.setText(item.getSnippet().getTitle());
        channel.setText(item.getSnippet().getChannelTitle());
        new DownloadImageTask(imageView)
                .execute(item.getSnippet().getThumbnails().getHigh().getUrl());
    }

}
