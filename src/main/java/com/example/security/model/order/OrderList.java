package com.example.security.model.order;

import java.util.List;

public class OrderList {
    private Order openOrder;
    private List<Order> closedOrders;

    public OrderList() {
    }

    public OrderList(Order openOrder, List<Order> closedOrders) {
        this.openOrder = openOrder;
        this.closedOrders = closedOrders;
    }

    public Order getOpenOrder() {
        return openOrder;
    }

    public void setOpenOrder(Order openOrder) {
        this.openOrder = openOrder;
    }

    public List<Order> getClosedOrders() {
        return closedOrders;
    }

    public void setClosedOrders(List<Order> closedOrders) {
        this.closedOrders = closedOrders;
    }

    @Override
    public String toString() {
        return "OrderList{" +
                "openOrder=" + openOrder +
                ", closedOrders=" + closedOrders +
                '}';
    }
}
