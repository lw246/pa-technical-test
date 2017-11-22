package models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Video {
    @SerializedName("_id")
    public String _id;

    @SerializedName("artist")
    public String artist;

    @SerializedName("song")
    public String song;

    @SerializedName("publishDate")
    public String publishDate;

    @SerializedName("__v")
    public Integer __v;

    @SerializedName("date_created")
    public String date_created;

    public String hidden;
}

