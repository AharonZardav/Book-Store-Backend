package com.example.security.model.order;

import com.example.security.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private List<OrderItem> items;

    public Order() {
    }

    public Order(OrderRequest orderRequest){
        this.username = orderRequest.getUsername();
        this.shippingAddress = orderRequest.getShippingAddress();
        this.items = new ArrayList<>();
    }

    public Order(int orderId, String username, LocalDate orderDate, String shippingAddress, Status orderStatus) {
        this.orderId = orderId;
        this.username = username;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
        this.orderStatus = orderStatus;
        this.items = new ArrayList<>();
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

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

//    public void addNewItemToOrder(OrderItem item) {
//        if (this.items == null) {
//            this.items = new ArrayList<>();
//        }
//        this.items.add(item);
//    }
//
//    public void removeExistingItemFromOrder(int itemId) {
//        if (this.items != null) {
//            this.items.removeIf(item -> item.getItemId() == itemId);
//        }
//    }
//
//    public void increaseItemQuantityInOrder(int itemId, int quantityToIncrease) {
//        if (this.items != null){
//            for(OrderItem orderItem : this.items){
//                if (orderItem.getItemId() == itemId){
//                    orderItem.increaseQuantityInOrder(quantityToIncrease);
//                }
//            }
//        }
//    }
//
//    public void reduceItemQuantityInOrder(int itemId, int quantityToReduce) {
//        if (this.items != null){
//            for(OrderItem orderItem : this.items){
//                if (orderItem.getItemId() == itemId){
//                    orderItem.reduceQuantityInOrder(quantityToReduce);
//                }
//            }
//        }
//    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", username='" + username + '\'' +
                ", orderDate=" + orderDate +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", orderStatus=" + orderStatus +
                ", items=" + items +
                '}';
    }
}
