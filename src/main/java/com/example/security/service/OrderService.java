package com.example.security.service;

import com.example.security.model.*;
import com.example.security.model.item.Item;
import com.example.security.model.order.Order;
import com.example.security.model.order.OrderItem;
import com.example.security.model.order.OrderList;
import com.example.security.model.order.OrderRequest;
import com.example.security.model.user.CustomUser;
import com.example.security.repository.OrderRepository;
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
        //Checking that there are no empty fields
        if (orderRequest.getUsername() == null || orderRequest.getShippingAddress() == null){
            return "Order not created: username and shipping address are required";
        }
        CustomUser user = userService.getUserByUsername(orderRequest.getUsername());
        if (user == null){
            return "Order not created: User with this username does not exist";
        }

        //Input integrity check
        String addressValidateResult = Validation.validateAddress(orderRequest.getShippingAddress());
        if (addressValidateResult != null){
            return "Order not created: "+addressValidateResult;
        }

        Order openOrder = orderRepository.findOpenUserOrder(user.getUsername());
        if (openOrder != null){
            return "Order not created: You already have an open order, a new order cannot be opened";
        }

        Order order = new Order(orderRequest);
        return orderRepository.createNewOrder(order);
    }

    public String addItemToOrder(String username, String title, int quantityInOrder, String shippingAddress){
        Order openOrder = orderRepository.findOpenUserOrder(username);
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
        }

        //Input integrity check
        Order currentOrder = orderRepository.findOpenUserOrder(username);
        int orderId = currentOrder.getOrderId();

        if (!orderRepository.isOrderBelongToUser(username, orderId)) {
            return "Item not added: You are not allowed to add item to an order that does not belong to you";
        }

        if (title == null){
            return "Item not removed: Item title is required";
        }
        Item item = itemService.getItemByTitle(title);
        if (item == null) {
            return "Item not added: Item with this title dose not exist";
        }
        if (quantityInOrder <= 0) {
            return "Item not added: Quantity must be at least 1";
        }
        int quantityInStock = itemService.getItemByTitle(title).getQuantity();
        if (quantityInStock < quantityInOrder) {
            return "Item not added: Not enough items in stock";
        }

        int itemId = item.getItemId();
        if (orderRepository.isItemInOrder(orderId, itemId)) {
            int itemQuantityInStock = item.getQuantity();
            int existingQuantityInOrder = orderRepository.findItemQuantityInOrder(orderId, itemId);
            int updatedQuantity = existingQuantityInOrder + quantityInOrder;
            if (itemQuantityInStock < updatedQuantity) {
                return "Item not added: Not enough items in stock";
            }
            return orderRepository.updateItemQuantityInOrder(orderId, itemId, updatedQuantity);
        }
        return orderRepository.addItemToOrder(orderId, itemId, quantityInOrder);
    }

    public String removeItemFromOrder(String username, String title, int quantityToRemoveFromOrder){
        //Input integrity check
        Order openOrder = orderRepository.findOpenUserOrder(username);
        if (openOrder == null){
            return "Item not removed: You cant remove item if you don't have an open order";
        } else {
            int orderId = openOrder.getOrderId();
            List<OrderItem> items = orderRepository.findOrderItems(orderId);
            openOrder.setItems(items);

            if (quantityToRemoveFromOrder <= 0) {
                return "Item not removed: Quantity must be at least 1";
            }

            if (title == null){
                return "Item not removed: Item title is required";
            }
            Item item = itemService.getItemByTitle(title);
            if (item == null) {
                return "Item not added: Item with this title dose not exist";
            }
            int itemId = item.getItemId();
            int existingQuantityInOrder = orderRepository.findItemQuantityInOrder(orderId, itemId);
            if (existingQuantityInOrder < quantityToRemoveFromOrder) {
                return "Item not removed: Not enough items in order";
            }

            if (!orderRepository.isItemInOrder(orderId, itemId)) {
                return "Item not removed: You don't have this item in your order";
            }

            int updatedQuantity = existingQuantityInOrder - quantityToRemoveFromOrder;
            if (updatedQuantity == 0) {
                String removeItemFromOrder = orderRepository.removeItemFromOrder(orderId, itemId);
                if (removeItemFromOrder.contains("successfully") && openOrder.getItems().isEmpty()) {
                    String closeOrder = closeOrder(username);
                    if (closeOrder == null) {
                        return "Order not closed: Unknown error occurred";
                    }
                    if (closeOrder.contains("successfully")) {
                        String deleteEmptyOrder = orderRepository.deleteEmptyOrder(username, orderId);
                        return "There are no items left in the order: " + closeOrder + ". " + deleteEmptyOrder;
                    }
                }
            }
            return orderRepository.updateItemQuantityInOrder(orderId, itemId, updatedQuantity);
        }
    }

    public String closeOrder(String username){
        OrderList userOrders = getAllUserOrders(username);
        if (userOrders == null){
            return "Order not closed: You don't have orders yet";
        }

        //handle open order
        Order openOrder = userOrders.getOpenOrder();
        if (openOrder == null){
            return "Order not closed: You cant close order if you don't have an open order";
        }
        List<OrderItem> itemsInOpenOrder= openOrder.getItems();
        if (itemsInOpenOrder == null){
            return "Order not closed: You don't have items in your order";
        }
        for (OrderItem orderItem : itemsInOpenOrder){
            int itemId = orderItem.getItemId();
            int quantityInOrder = orderItem.getQuantityInOrder();
            String buyItem = itemService.buyItem(itemId, quantityInOrder);
            if (buyItem.contains("failed")){
                return buyItem;
            }
        }

        //handle closed orders
        List<Order> closedOrders = userOrders.getClosedOrders();
        for (Order closeOrder : closedOrders){
            List<OrderItem> itemsInClosedOrder = closeOrder.getItems();
            for (OrderItem orderItem : itemsInClosedOrder){
                int itemId = orderItem.getItemId();
                int quantityInOrder = orderItem.getQuantityInOrder();
                String buyItem = itemService.buyItem(itemId, quantityInOrder);
                if (buyItem.contains("failed")){
                    return buyItem;
                }
            }
        }
        return orderRepository.closeOpenOrder(username);
    }

    public OrderList getAllUserOrders(String username){
        Order openOrder = orderRepository.findOpenUserOrder(username);
        if (openOrder != null){
            int orderId = openOrder.getOrderId();
            List<OrderItem> items = orderRepository.findOrderItems(orderId);
            openOrder.setItems(items);
        }
        List<Order> closedOrders = orderRepository.findAllClosedUserOrders(username);
        if (!closedOrders.isEmpty()){
            for (Order closedOrder : closedOrders){
                int orderId = closedOrder.getOrderId();
                List<OrderItem> items = orderRepository.findOrderItems(orderId);
                closedOrder.setItems(items);
            }
        }
        return new OrderList(openOrder, closedOrders);
    }
}