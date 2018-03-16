package com.example.guill.youtube.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.guill.youtube.models.Item;
import com.example.guill.youtube.R;
import com.example.guill.youtube.RecyclerItemClickListener;
import com.example.guill.youtube.models.SOAnswersResponse;
import com.example.guill.youtube.adapters.VideoRecyclerAdapter;
import com.example.guill.youtube.interfaces.YoutubeAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String key = "AIzaSyCU-Al-NuszNKX9QRiDiJ9E7BI_7Aa-MXk";
    private String part = "snippet";

    private RecyclerView recyclerView;
    private VideoRecyclerAdapter adapter;
    private List<Item> videos;

    private static final String PREFS = "PREFS";
    private static final String PREFS_SEARCH = "PREFS_SEARCH";
    SharedPreferences sharedPreferences;

    private Context parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);

        videos = new ArrayList<Item>();
        initializeRecyclerView();
        initializeAdapter(videos);
        youtubeAPICall("");

        parent = this;

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

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menu_search = menu.findItem(R.id.menu_search);
        MenuItem menu_home = menu.findItem(R.id.menu_home);

        menu_home.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                youtubeAPICall("");
                return false;
            }
        });

        menu_search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                item.getActionView().requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
        });

        SearchView searchView = (SearchView) menu_search.getActionView();

        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
        searchAutoComplete.setThreshold(0);

        // make searchview results occupy whole screen
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) searchAutoComplete.findViewById(R.id.search_src_text);
        final View dropDownAnchor = searchView.findViewById(autoCompleteTextView.getDropDownAnchor());
        if (dropDownAnchor != null) {
            dropDownAnchor.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    Rect screenSize = new Rect();
                    getWindowManager().getDefaultDisplay().getRectSize(screenSize);
                    autoCompleteTextView.setDropDownWidth(screenSize.width());
                }
            });
        }

        if(sharedPreferences.contains(PREFS_SEARCH)) {
            final String dataArr[] = sharedPreferences.getString(PREFS_SEARCH, null).split(";");
            ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<String>(this, R.layout.dropdown, dataArr);
            searchAutoComplete.setAdapter(autocompleteAdapter);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                youtubeAPICall(query);

                if(sharedPreferences.contains(PREFS_SEARCH)) {
                    // check if this query already exist
                    if(!Arrays.asList(sharedPreferences.getString(PREFS_SEARCH, null).split(";")).contains(query)){
                        sharedPreferences
                                .edit()
                                .putString(PREFS_SEARCH, sharedPreferences.getString(PREFS_SEARCH, null) + ";" + query)
                                .apply();
                    }
                } else {
                    sharedPreferences
                            .edit()
                            .putString(PREFS_SEARCH, query)
                            .apply();
                }

                // update autocomplete
                String newDataArr[] = sharedPreferences.getString(PREFS_SEARCH, null).split(";");
                ArrayAdapter<String> newAutocompleteAdapter = new ArrayAdapter<String>(parent, R.layout.dropdown, newDataArr);
                searchAutoComplete.setAdapter(newAutocompleteAdapter);

                // hide keybord
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(findViewById(R.id.recyclerView).getWindowToken(), 0);

                return true;
            }
        });

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
            }
        });

        return true;
    }


    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeAdapter(List<Item> videos) {
        adapter = new VideoRecyclerAdapter(videos);
        recyclerView.setAdapter(adapter);
    }

    private void youtubeAPICall(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeAPI youtubeAPI = retrofit.create(YoutubeAPI.class);
        Call<SOAnswersResponse> call = youtubeAPI.getAnswers(key, part, query, "video", 20);
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
                Toast.makeText(parent, "Network error: failed to load the videos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
