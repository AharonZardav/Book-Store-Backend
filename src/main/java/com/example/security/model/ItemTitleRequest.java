package com.example.security.model;

public class ItemTitleRequest {
    String item_title;

    public ItemTitleRequest() {
    }

    public ItemTitleRequest(String item_title) {
        this.item_title = item_title;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    @Override
    public String toString() {
        return "ItemTitle{" +
                "item_title='" + item_title + '\'' +
                '}';
    }
}