package com.example.priyamgupta.android_boilerplate.screens.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.priyamgupta.android_boilerplate.GitHubApplication;
import com.example.priyamgupta.android_boilerplate.R;
import com.example.priyamgupta.android_boilerplate.models.Repo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SplashActivity2 extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private static String TAG = SplashActivity2.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new FetchData().execute("hii");
    }

    private class FetchData extends AsyncTask<String, Void, Repo> {

        @Override
        protected Repo doInBackground(String... params) {
            Log.d(TAG, "doInBackground : " + params[0]);
            GsonBuilder gsonBuilder  = new GsonBuilder();
// Allowing the serialization of static fields
            gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
// Creates a Gson instance based on the current configuration
            Gson gson = gsonBuilder.create();
            String json = null;
            try {
                json = makeRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Repo list = gson.fromJson(json, new TypeToken<Repo>(){}.getType());
            Repo r = gson.fromJson(json, Repo.class);

            Log.d(TAG, "REPONSE : " + json);
            Log.d(TAG, "REPONSE 1: " + gson.toJson(list));
            Log.d(TAG, "REPONSE 2: " + gson.toJson(r));
            return list;
        }
        private String makeRequest() throws IOException {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.github.com/users/priyam")
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        @Override
        protected void onPostExecute(Repo result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute" + result);
            // After completing http call
            // will close this activity and lauch main activity
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
//            i.putParcelableArrayListExtra("LIST", result);
            startActivity(i);

            // close this activity
            finish();
        }
    }
}
