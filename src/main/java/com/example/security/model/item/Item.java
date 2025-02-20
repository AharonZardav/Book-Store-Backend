package com.example.security.model.item;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    @JsonProperty(value = "item_id")
    private int itemId;
    private String title;
    @JsonProperty(value = "image_path")
    private String imagePath;
    private double price;
    private int quantity;

    public Item() {
    }

    public Item(int itemId, String title, String imagePath, double price, int quantity) {
        this.itemId = itemId;
        this.title = title;
        this.imagePath = imagePath;
        this.price = price;
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int id) {
        this.itemId = id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", title='" + title + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
