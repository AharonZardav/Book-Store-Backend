package com.example.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private int id;
    private String title;
    @JsonProperty(value = "image_path")
    private String imagePath;
    private int price;
    private int quantity;

    public Item() {
    }

    public Item(int id, String title, String imagePath, int price, int quantity) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
