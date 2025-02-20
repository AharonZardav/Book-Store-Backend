package com.example.security.model.item;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemRequest {
    private String username;
    private String title;
    private int quantity;
    @JsonProperty(value = "shipping_address")
    private String shippingAddress;

    public ItemRequest() {
    }

    public ItemRequest(String username, String title, int quantity, String shippingAddress) {
        this.username = username;
        this.title = title;
        this.quantity = quantity;
        this.shippingAddress = shippingAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userUsername) {
        this.username = userUsername;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantityInOrder) {
        this.quantity = quantityInOrder;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Override
    public String toString() {
        return "ItemRequest{" +
                "username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", quantity=" + quantity +
                ", shippingAddress='" + shippingAddress + '\'' +
                '}';
    }
}
