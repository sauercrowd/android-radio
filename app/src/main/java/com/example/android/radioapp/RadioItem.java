package com.example.android.radioapp;

import java.io.Serializable;

/**
 * Created by jotten on 14.11.17.
 */

public class RadioItem implements Serializable{
    private String title;
    private String imageURL;
    private String url;
    private String stream_url;

    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
