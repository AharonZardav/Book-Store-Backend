package com.example.security.service;

import com.example.security.model.*;
import com.example.security.model.item.Item;
import com.example.security.model.order.Order;
import com.example.security.model.order.OrderList;
import com.example.security.model.order.OrderRequest;
import com.example.security.model.user.CustomUser;
import com.example.security.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        openOrder = orderRepository.findOpenUserOrder(username);
        List<Item> openOrderItems = orderRepository.findOrderItems(openOrder.getOrderId());
        //Input integrity check
        if (!orderRepository.isOrderBelongToUser(username, openOrder.getOrderId())) {
            if (openOrderItems.isEmpty()){
                String deleteEmptyOrder = orderRepository.deleteEmptyOrder(username, openOrder.getOrderId());
                return "There are no items left in the order: " + ". " + deleteEmptyOrder;
            }
            return "Item not added: You are not allowed to add item to an order that does not belong to you";
        }

        if (title == null){
            if (openOrderItems.isEmpty()){
                String deleteEmptyOrder = orderRepository.deleteEmptyOrder(username, openOrder.getOrderId());
                return "There are no items left in the order: " + ". " + deleteEmptyOrder;
            }
            return "Item not added: Item title is required";
        }
        Item item = itemService.getItemByTitle(title);
        if (item == null) {
            if (openOrderItems.isEmpty()){
                String deleteEmptyOrder = orderRepository.deleteEmptyOrder(username, openOrder.getOrderId());
                return "There are no items left in the order: " + ". " + deleteEmptyOrder;
            }
            return "Item not added: Item with this title dose not exist";
        }
        if (quantityInOrder <= 0) {
            if (openOrderItems.isEmpty()){
                String deleteEmptyOrder = orderRepository.deleteEmptyOrder(username, openOrder.getOrderId());
                return "There are no items left in the order: " + ". " + deleteEmptyOrder;
            }
            return "Item not added: Quantity must be at least 1";
        }
        int quantityInStock = itemService.getItemByTitle(title).getQuantity();
        if (quantityInStock < quantityInOrder) {
            if (openOrderItems.isEmpty()){
                String deleteEmptyOrder = orderRepository.deleteEmptyOrder(username, openOrder.getOrderId());
                return "There are no items left in the order: " + ". " + deleteEmptyOrder;
            }
            return "Item not added: Not enough items in stock";
        }

        int itemId = item.getItemId();
        if (orderRepository.isItemInOrder(openOrder.getOrderId(), itemId)) {
            int itemQuantityInStock = item.getQuantity();
            int existingQuantityInOrder = orderRepository.findItemQuantityInOrder(openOrder.getOrderId(), itemId);
            int updatedQuantity = existingQuantityInOrder + quantityInOrder;
            if (itemQuantityInStock < updatedQuantity) {
                if (openOrderItems.isEmpty()){
                    String deleteEmptyOrder = orderRepository.deleteEmptyOrder(username, openOrder.getOrderId());
                    return "There are no items left in the order: " + ". " + deleteEmptyOrder;
                }
                return "Item not added: Not enough items in stock";
            }
            return orderRepository.updateItemQuantityInOrder(openOrder.getOrderId(), itemId, updatedQuantity);
        }
        return orderRepository.addItemToOrder(openOrder.getOrderId(), itemId, quantityInOrder);
    }

    public String removeItemFromOrder(String username, String title, int quantityToRemoveFromOrder){
        //Input integrity check
        Order openOrder = orderRepository.findOpenUserOrder(username);
        if (openOrder == null){
            return "Item not removed: You cant remove item if you don't have an open order";
        } else {
            int orderId = openOrder.getOrderId();
            List<Item> items = orderRepository.findOrderItems(orderId);
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
                if (removeItemFromOrder.contains("successfully")){
                    List<Item> openOrderItems = orderRepository.findOrderItems(openOrder.getOrderId());
                    if (openOrderItems.isEmpty()) {
                        String deleteEmptyOrder = orderRepository.deleteEmptyOrder(username, orderId);
                        return "There are no items left in the order: " + ". " + deleteEmptyOrder;
                    }
                }
            }
            return orderRepository.updateItemQuantityInOrder(orderId, itemId, updatedQuantity);
        }
    }

    public String closeOrder(String username){

        Order openOrder = orderRepository.findOpenUserOrder(username);
        if (openOrder == null){
            return "Order not closed: You cant close order if you don't have an open order";
        }
        List<Item> itemsInOpenOrder= orderRepository.findOrderItems(openOrder.getOrderId());
        if (itemsInOpenOrder == null || itemsInOpenOrder.isEmpty()){
            return "Order not closed: You don't have items in your order";
        }
        for (Item orderItem : itemsInOpenOrder){
            int itemId = orderItem.getItemId();
            int quantityInOrder = orderItem.getQuantity();
            String buyItem = itemService.buyItem(itemId, quantityInOrder);
            if (buyItem.contains("failed")){
                return buyItem;
            }
        }

        //handle closed orders
//        List<Order> closedOrders = userOrders.getClosedOrders();
//        for (Order closeOrder : closedOrders){
//            List<Item> itemsInClosedOrder = closeOrder.getItems();
//            for (Item orderItem : itemsInClosedOrder){
//                int itemId = orderItem.getItemId();
//                int quantityInOrder = orderItem.getQuantity();
//                String buyItem = itemService.buyItem(itemId, quantityInOrder);
//                if (buyItem.contains("failed")){
//                    return buyItem;
//                }
//            }
//        }
        return orderRepository.closeOpenOrder(username);
    }

    public OrderList getAllUserOrders(String username){
        Order openOrder = orderRepository.findOpenUserOrder(username);
        if (openOrder != null){
            int orderId = openOrder.getOrderId();
            List<Item> items = orderRepository.findOrderItems(orderId);
            openOrder.setItems(items);
        }
        List<Order> closedOrders = orderRepository.findAllClosedUserOrders(username);
        if (!closedOrders.isEmpty()){
            for (Order closedOrder : closedOrders){
                int orderId = closedOrder.getOrderId();
                List<Item> items = orderRepository.findOrderItems(orderId);
                closedOrder.setItems(items);
            }
        }
        return new OrderList(openOrder, closedOrders);
    }
}