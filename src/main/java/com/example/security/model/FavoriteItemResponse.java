package com.example.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FavoriteItemResponse {
    @JsonProperty(value = "item_id")
    int itemId;
    String title;

    public FavoriteItemResponse() {
    }

    public FavoriteItemResponse(int itemId, String title) {
        this.itemId = itemId;
        this.title = title;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "FavoriteItemResponse{" +
                "itemId=" + itemId +
                ", title='" + title + '\'' +
                '}';
    }
}
