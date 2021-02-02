package com.example.android.multiactivitygithubsearch.data;

import com.google.gson.annotations.SerializedName;

public class GitHubRepo {
    @SerializedName("full_name")
    public String fullName;

    public String description;

    @SerializedName("html_url")
    public String htmlUrl;

    @SerializedName("stargazers_count")
    public int stars;
}
