package com.example.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Order {
    @JsonProperty(value = "order_id")
    private int orderId;
    private String username;
    @JsonProperty(value = "order_date")
    private LocalDate orderDate;
    @JsonProperty(value = "shipping_address")
    private String shippingAddress;
    @JsonProperty(value = "order_status")
    private Status orderStatus;

    public Order() {
    }

    public Order(int orderId, String username, LocalDate orderDate, String shippingAddress, Status orderStatus) {
        this.orderId = orderId;
        this.username = username;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }
}
