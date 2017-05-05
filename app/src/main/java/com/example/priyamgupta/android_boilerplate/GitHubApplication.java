package com.example.priyamgupta.android_boilerplate;

import android.app.Application;
import android.text.method.DateTimeKeyListener;

import com.example.priyamgupta.android_boilerplate.networks.GitHubService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by priyamgupta on 30/04/17.
 */

public class GitHubApplication extends Application {

    private GitHubService gitHubService;
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        });

        File cacheFile = new File(getCacheDir(), "okhttp-cache");
        cacheFile.mkdir();
        Cache cache = new Cache(cacheFile, 10 * 1000 * 1000);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();

        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();


        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(DateTypeAdapter.class, new DateTypeAdapter());
        Gson gson = builder.create();


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl("https://api.github.com/")
                .build();

    }
}
