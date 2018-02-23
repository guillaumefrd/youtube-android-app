package com.example.guill.youtube;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

    private String key = "AIzaSyCU-Al-NuszNKX9QRiDiJ9E7BI_7Aa-MXk";
    private String part = "snippet";

    private RecyclerView recyclerView;
    private VideoRecyclerAdapter adapter;
    private List<Item> videos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText searchBox = findViewById(R.id.searchBox);
        searchBox.setOnKeyListener(this);

        videos = new ArrayList<Item>();
        initializeRecyclerView();
        initializeAdapter(videos);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, VideoDetailedActivity.class);
                        intent.putExtra("VIDEO", videos.get(position));
                        startActivity(intent);
                    }
                })
        );
    }

    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeAdapter(List<Item> videos) {
        adapter = new VideoRecyclerAdapter(videos);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            if (!event.isShiftPressed()) {

                EditText searchBox = findViewById(R.id.searchBox);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://www.googleapis.com/youtube/v3/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                YoutubeAPI youtubeAPI = retrofit.create(YoutubeAPI.class);
                Call<SOAnswersResponse> call = youtubeAPI.getAnswers(key, part, searchBox.getText().toString(), "video", 6);
                call.enqueue(new Callback<SOAnswersResponse>() {
                    @Override
                    public void onResponse(Call<SOAnswersResponse> call, Response<SOAnswersResponse> response) {
                        videos = response.body().getItems();
                        adapter.getVideos().clear();
                        adapter.getVideos().addAll(videos);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<SOAnswersResponse> call, Throwable t) {

                    }
                });
                return true;
            }

        }
        return false;

    }
}
