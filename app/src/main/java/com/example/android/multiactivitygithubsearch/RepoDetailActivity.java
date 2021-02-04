package com.example.android.multiactivitygithubsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.example.android.multiactivitygithubsearch.data.GitHubRepo;

public class RepoDetailActivity extends AppCompatActivity {
    public static final String EXTRA_GITHUB_REPO = "GitHubRepo";

    private static final String TAG = RepoDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_GITHUB_REPO)) {
            GitHubRepo repo = (GitHubRepo) intent.getSerializableExtra(EXTRA_GITHUB_REPO);
            Log.d(TAG, "Got repo with name: " + repo.fullName);

            TextView repoNameTV = findViewById(R.id.tv_repo_name);
            TextView repoStarsTV = findViewById(R.id.tv_repo_stars);
            TextView repoDescriptionTV = findViewById(R.id.tv_repo_description);

            repoNameTV.setText(repo.fullName);
            repoStarsTV.setText(String.valueOf(repo.stars));
            repoDescriptionTV.setText(repo.description);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.repo_detail, menu);
        return true;
    }
}