package com.example.android.multiactivitygithubsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.multiactivitygithubsearch.data.GitHubRepo;
import com.example.android.multiactivitygithubsearch.utils.GitHubUtils;
import com.example.android.multiactivitygithubsearch.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GitHubSearchAdapter.OnSearchResultClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView searchResultsRV;
    private EditText searchBoxET;
    private ProgressBar loadingIndicatorPB;
    private TextView errorMessageTV;

    private GitHubSearchAdapter githubSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.searchBoxET = findViewById(R.id.et_search_box);
        this.searchResultsRV = findViewById(R.id.rv_search_results);
        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = findViewById(R.id.tv_error_message);

        this.searchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        this.searchResultsRV.setHasFixedSize(true);

        this.githubSearchAdapter = new GitHubSearchAdapter(this);
        this.searchResultsRV.setAdapter(this.githubSearchAdapter);

        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    doGitHubSearch(searchQuery);
                }
            }
        });
    }

    private void doGitHubSearch(String query) {
        String url = GitHubUtils.buildGitHubSearchURL(query);
        new GitHubSearchTask().execute(url);
    }

    @Override
    public void onSearchResultClicked(GitHubRepo repo) {
        Log.d(TAG, "Search result clicked: " + repo.fullName);
    }

    public class GitHubSearchTask extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicatorPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            Log.d(TAG, "searching with this URL: " + url);

            String results = null;
            try {
                results = NetworkUtils.doHttpGet(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "query results: " + results);
            return results;
        }

        @Override
        protected void onPostExecute(String results) {
            loadingIndicatorPB.setVisibility(View.INVISIBLE);
            if (results != null) {
                ArrayList<GitHubRepo> searchResultsList = GitHubUtils.parseGitHubSearchResults(results);
                githubSearchAdapter.updateSearchResults(searchResultsList);
                searchResultsRV.setVisibility(View.VISIBLE);
                errorMessageTV.setVisibility(View.INVISIBLE);
            } else {
                searchResultsRV.setVisibility(View.INVISIBLE);
                errorMessageTV.setVisibility(View.VISIBLE);
            }
        }
    }
}