package com.example.guill.youtube;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoDetailedActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private String key = "AIzaSyCU-Al-NuszNKX9QRiDiJ9E7BI_7Aa-MXk";
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private Item video;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detailed);
        video = getIntent().getExtras().getParcelable("VIDEO");

        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.description);
        TextView channel = findViewById(R.id.channel);
        title.setText(video.getSnippet().getTitle());
        description.setText(video.getSnippet().getDescription());
        channel.setText(video.getSnippet().getChannelTitle());

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(key, this);
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(video.getId().getVideoId());
        }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(key, this);
        }
    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }
}
