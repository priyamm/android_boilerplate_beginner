package com.example.priyamgupta.android_boilerplate.networks;

import com.example.priyamgupta.android_boilerplate.models.Repo;
import com.example.priyamgupta.android_boilerplate.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by priyamgupta on 30/04/17.
 */

public interface GitHubService {
    @GET("users/{username}/repos")
    Call<List<Repo>> listRepos(@Path("username") String username);

    @GET("repositories")
    Call<List<Repo>> getAllRepos();

    @GET("users/{username}")
    Call<User> getUser(@Path("username") String username);
}
