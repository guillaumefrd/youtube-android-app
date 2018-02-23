package com.example.guill.youtube;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView description;
    private ImageView imageView;

    public VideoViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }

    public void bind(Item item) {
        title.setText(item.getSnippet().getTitle());
        description.setText(item.getSnippet().getDescription());
        new DownloadImageTask(imageView)
                .execute(item.getSnippet().getThumbnails().getHigh().getUrl());
    }

}
