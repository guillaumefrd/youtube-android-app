package com.example.guill.youtube.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guill.youtube.R;
import com.example.guill.youtube.models.Item;
import com.squareup.picasso.Picasso;

public class VideoViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView channel;
    private ImageView imageView;

    public VideoViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        channel = itemView.findViewById(R.id.channel);
        imageView = itemView.findViewById(R.id.imageView);
    }

    public void bind(Item item) {
        title.setText(item.getSnippet().getTitle());
        channel.setText(item.getSnippet().getChannelTitle());
        Picasso.get().load(item.getSnippet().getThumbnails().getHigh().getUrl()).into(imageView);
    }
}
