package com.example.priyamgupta.android_boilerplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by priyamgupta on 30/04/17.
 */

public class Repo {
    @Expose
    @SerializedName("id")
    private Integer id=null;
    @Expose
    @SerializedName("name")
    private String name;


    @SerializedName("full_name")@Expose
    private String full_name;
    @Expose
    @SerializedName("login")
    private static String login=null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        Repo.login = login;
    }
}
