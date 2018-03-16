package com.example.guill.youtube.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snippet implements Parcelable {
    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;
    @SerializedName("channelId")
    @Expose
    private String channelId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("thumbnails")
    @Expose
    private Thumbnails thumbnails;
    @SerializedName("channelTitle")
    @Expose
    private String channelTitle;
    @SerializedName("liveBroadcastContent")
    @Expose
    private String liveBroadcastContent;

    protected Snippet(Parcel in) {
        publishedAt = in.readString();
        channelId = in.readString();
        title = in.readString();
        description = in.readString();
        channelTitle = in.readString();
        liveBroadcastContent = in.readString();
    }

    public static final Creator<Snippet> CREATOR = new Creator<Snippet>() {
        @Override
        public Snippet createFromParcel(Parcel in) {
            return new Snippet(in);
        }

        @Override
        public Snippet[] newArray(int size) {
            return new Snippet[size];
        }
    };

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Thumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Thumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getLiveBroadcastContent() {
        return liveBroadcastContent;
    }

    public void setLiveBroadcastContent(String liveBroadcastContent) {
        this.liveBroadcastContent = liveBroadcastContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(publishedAt);
        parcel.writeString(channelId);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(channelTitle);
        parcel.writeString(liveBroadcastContent);
    }
}
