package com.example.security.service;

import com.example.security.model.*;
import com.example.security.model.enums.Status;
import com.example.security.model.item.Item;
import com.example.security.model.order.Order;
import com.example.security.model.order.OrderList;
import com.example.security.model.order.OrderRequest;
import com.example.security.model.user.CustomUser;
import com.example.security.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    public String createNewOrder(OrderRequest orderRequest){
        String addressValidateResult = Validation.validateAddress(orderRequest.getShippingAddress());
        if (addressValidateResult != null){
            return "Order not created: "+addressValidateResult;
        }
        Order order = new Order(orderRequest);
        return orderRepository.createNewOrder(order);
    }

    public int itemQuantityInOrder(Order order, int itemId){
        List<Item> orderItems = order.getItems();
        for (Item orderItem : orderItems){
            if(orderItem.getItemId() == itemId){
                return orderItem.getQuantity();
            }
        }
        return 0;
    }

    public String addItemToOrder(String username, String title, int quantityInOrder, String shippingAddress){
        Order openOrder = orderRepository.findOpenUserOrderWithItems(username);
        boolean newOrder = false;
        if (openOrder == null){
            if (shippingAddress == null || shippingAddress.isEmpty()){
                return "Item not added: you don't have an open order yet. please provide shipping address also";
            }
            OrderRequest orderRequest = new OrderRequest(username, shippingAddress);
            String createNewOrder = createNewOrder(orderRequest);
            if (createNewOrder == null){
                return "failed to create new order: reason not known";
            }
            if (createNewOrder.contains("not created")){
                return createNewOrder;
            }
            openOrder = orderRepository.findOpenUserOrderWithItems(username);
            newOrder = true;
        }

        //Input integrity check
        if (!orderRepository.isOrderBelongToUser(username, openOrder.getOrderId())) {
            if (newOrder){
                orderRepository.deleteEmptyOrder(username, openOrder.getOrderId());
            }
            return "Item not added: You are not allowed to add item to an order that does not belong to you";
        }

        if (title == null){
            if (newOrder){
                orderRepository.deleteEmptyOrder(username, openOrder.getOrderId());
            }
            return "Item not added: Item title is required";
        }

        Item item = itemService.getItemByTitle(title);
        if (item == null) {
            if (newOrder){
                orderRepository.deleteEmptyOrder(username, openOrder.getOrderId());
            }
            return "Item not added: Item with this title dose not exist";
        }

        if (quantityInOrder <= 0) {
            if (newOrder){
                orderRepository.deleteEmptyOrder(username, openOrder.getOrderId());
            }
            return "Item not added: Quantity must be at least 1";
        }

        if (item.getQuantity() < quantityInOrder) {
            if (newOrder){
                orderRepository.deleteEmptyOrder(username, openOrder.getOrderId());
            }
            return "Item not added: Not enough items in stock";
        }

        int itemId = item.getItemId();
        int existingQuantityInOrder = itemQuantityInOrder(openOrder, itemId);
        if (existingQuantityInOrder > 0) {
            int updatedQuantity = existingQuantityInOrder + quantityInOrder;
            if (item.getQuantity() < updatedQuantity) {
                return "Item not added: Not enough items in stock";
            }
            return orderRepository.updateItemQuantityInOrder(openOrder.getOrderId(), itemId, updatedQuantity);
        }
        return orderRepository.addItemToOrder(openOrder.getOrderId(), itemId, quantityInOrder);
    }

    public String removeItemFromOrder(String username, String title, int quantityToRemoveFromOrder){
        //Input integrity check
        Order openOrder = orderRepository.findOpenUserOrderWithItems(username);
        if (openOrder == null){
            return "Item not removed: You cant remove item if you don't have an open order";
        } else {
            if (quantityToRemoveFromOrder <= 0) {
                return "Item not removed: Quantity must be at least 1";
            }

            if (title == null){
                return "Item not removed: Item title is required";
            }

            Item item = itemService.getItemByTitle(title);
            if (item == null) {
                return "Item not removed: Item with this title dose not exist";
            }

            int itemId = item.getItemId();
            int existingQuantityInOrder = itemQuantityInOrder(openOrder, itemId);
            if (existingQuantityInOrder < quantityToRemoveFromOrder) {
                return "Item not removed: You don't have enough items in order";
            }

            int orderId = openOrder.getOrderId();
            int updatedQuantityInOrder = existingQuantityInOrder - quantityToRemoveFromOrder;
            if (updatedQuantityInOrder == 0) {
                String removeItemFromOrder = orderRepository.removeItemFromOrder(orderId, itemId);
                if (removeItemFromOrder.contains("successfully")){
                    openOrder.getItems().removeIf(itemInOrder -> itemInOrder.getItemId() == itemId);
                    if (openOrder.getItems().isEmpty()) {
                        String deleteEmptyOrder = orderRepository.deleteEmptyOrder(username, orderId);
                        return "There are no items left in the order: " + ". " + deleteEmptyOrder;
                    }
                }
            }
            return orderRepository.updateItemQuantityInOrder(orderId, itemId, updatedQuantityInOrder);
        }
    }

    public String updateOrderAddress(String username, String shippingAddress){
        if(shippingAddress == null){
            return "Order shipping address not updated: Shipping address required";
        }

        String addressValidateResult = Validation.validateAddress(shippingAddress);
        if (addressValidateResult != null){
            return "Order address not updated: "+addressValidateResult;
        }

        Order openOrder = orderRepository.findOpenUserOrderWithItems(username);
        if (openOrder == null){
            return "Order address not updated: You don't have an open order";
        }

        return orderRepository.updateOpenOrderAddress(username, shippingAddress, openOrder.getOrderId());
    }

    public String closeOrder(String username){

        Order openOrder = orderRepository.findOpenUserOrderWithItems(username);
        if (openOrder == null){
            return "Order not closed: You cant close order if you don't have an open order";
        }
        if (openOrder.getItems() == null || openOrder.getItems().isEmpty()){
            return "Order not closed: You don't have items in your order";
        }
        for (Item orderItem : openOrder.getItems()){
            int itemId = orderItem.getItemId();
            int quantityInOrder = orderItem.getQuantity();
            String buyItem = itemService.buyItem(itemId, quantityInOrder);
            if (buyItem.contains("failed")){
                return buyItem;
            }
        }
        return orderRepository.closeOpenOrder(username);
    }

    public OrderList getAllUserOrders(String username){
//        Order openOrder = orderRepository.findOpenUserOrderWithItems(username);
//        List<Order> closedOrders = orderRepository.findAllClosedUserOrdersWithItems(username);

        List<Order> allOrders = orderRepository.findAllUserOrdersWithItems(username);
        Order openOrder = null;
        List<Order> closedOrders = new ArrayList<>();
        for (Order order : allOrders){
            if (order.getOrderStatus() == Status.OPEN){
                openOrder = order;
            } else {
                closedOrders.add(order);
            }
        }

        return new OrderList(openOrder, closedOrders);
    }
}