package com.example.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FavoriteItem {
    String username;
    @JsonProperty(value = "item_id")
    int itemId;

    public FavoriteItem() {
    }

    public FavoriteItem(String username, int itemId) {
        this.username = username;
        this.itemId = itemId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "FavoriteItem{" +
                "username='" + username + '\'' +
                ", itemId=" + itemId +
                '}';
    }
}
