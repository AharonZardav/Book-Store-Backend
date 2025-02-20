package com.example.security.model.order;

public class OrderItem {
    private int itemId;
    private int quantityInOrder;

    public OrderItem() {
    }

    public OrderItem(int itemId, int quantityInOrder) {
        this.itemId = itemId;
        this.quantityInOrder = quantityInOrder;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantityInOrder() {
        return quantityInOrder;
    }

    public void setQuantityInOrder(int quantityInOrder) {
        this.quantityInOrder = quantityInOrder;
    }

    public void increaseQuantityInOrder(int quantityInOrder) {
        this.quantityInOrder += quantityInOrder;
    }

    public void reduceQuantityInOrder(int updatedQuantityInOrder) {
        if (this.quantityInOrder > updatedQuantityInOrder) {
            this.quantityInOrder -= updatedQuantityInOrder;
        }
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "itemId=" + itemId +
                ", quantityInOrder=" + quantityInOrder +
                '}';
    }
}
