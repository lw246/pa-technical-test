package models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Video {
    @SerializedName("_id")
    public String Id;

    @SerializedName("artist")
    public String Artist;

    @SerializedName("song")
    public String Song;

    @SerializedName("publishDate")
    public Date PublishDate;

    @SerializedName("__v")
    public Integer V;

    @SerializedName("date_created")
    public Date DateCreated;
}
