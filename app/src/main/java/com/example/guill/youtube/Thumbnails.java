package com.example.guill.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnails {

    @SerializedName("high")
    @Expose
    private High high;

    public High getHigh() {
        return high;
    }

    public void setHigh(High high) {
        this.high = high;
    }
}
