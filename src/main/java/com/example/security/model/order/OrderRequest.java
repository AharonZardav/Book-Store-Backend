package com.example.security.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequest {
    private String username;
    @JsonProperty(value = "shipping_address")
    private String shippingAddress;

    public OrderRequest() {
    }

    public OrderRequest(String username, String shippingAddress) {
        this.username = username;
        this.shippingAddress = shippingAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "username='" + username + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                '}';
    }
}
