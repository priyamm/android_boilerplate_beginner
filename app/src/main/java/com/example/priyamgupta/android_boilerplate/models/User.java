package com.example.priyamgupta.android_boilerplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by priyamgupta on 30/04/17.
 */

public class User implements Parcelable {
    @SerializedName("id")
    private Integer id;
    @SerializedName("avatar_url")
    private String avatar_url;
    @SerializedName("url")
    private String url;

    public User(Parcel in) {
        avatar_url = in.readString();
        url = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatar_url);
        dest.writeString(url);
    }
}
