package com.example.mobilehome.owner;

import com.google.firebase.database.Exclude;

public class UploadImage1 {
    private String ImageUrl;
    private String Key;

    public UploadImage1() {

    }

    public UploadImage1(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {
        return Key;
    }

    @Exclude
    public void setKey(String key) {
        Key = key;
    }
}
