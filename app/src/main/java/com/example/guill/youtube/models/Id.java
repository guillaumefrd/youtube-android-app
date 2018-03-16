package com.example.guill.youtube.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Id implements Parcelable {
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("videoId")
    @Expose
    private String videoId;

    private Id(Parcel in) {
        kind = in.readString();
        videoId = in.readString();
    }


    public static final Creator<Id> CREATOR = new Creator<Id>() {
        @Override
        public Id createFromParcel(Parcel in) {
            return new Id(in);
        }

        @Override
        public Id[] newArray(int size) {
            return new Id[size];
        }
    };

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(kind);
        parcel.writeString(videoId);
    }
}
