package com.example.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FavoriteItemResponse {
    @JsonProperty(value = "item_id")
    int itemId;

    public FavoriteItemResponse() {
    }

    public FavoriteItemResponse(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "FavoriteItemResponse{" +
                "itemId=" + itemId +
                '}';
    }
}
