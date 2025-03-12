package com.example.security.repository;

import com.example.security.model.item.Item;
import com.example.security.model.order.Order;
import com.example.security.model.enums.Status;
import com.example.security.repository.mapper.ItemMapper;
import com.example.security.repository.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String ORDERS_TABLE = "orders";
    private static final String ORDERS_ITEMS_TABLE = "orders_items";
    private static final String ITEMS_TABLE = "items";


    public String createNewOrder(Order order) {
        try {
            String sql = String.format("INSERT INTO %s (username, order_date, shipping_address, order_status) VALUES (?,?,?,?)", ORDERS_TABLE);
            jdbcTemplate.update(sql, order.getUsername(), LocalDate.now(), order.getShippingAddress(), Status.OPEN.name());
            return "Order created successfully";
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String updateOpenOrderAddress(String username, String shippingAddress, int orderId){
        try {
            String sql = String.format("UPDATE %s SET shipping_address = ? WHERE username = ? AND order_id = ?", ORDERS_TABLE);
            jdbcTemplate.update(sql, shippingAddress, username, orderId);
            return "Shipping address in your order successfully updated";
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean isOrderBelongToUser(String username, int orderId){
        try {
            String sql = String.format("SELECT COUNT(*) FROM %s WHERE order_id = ? AND username = ?", ORDERS_TABLE);
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, orderId, username);
            return ((count != null) && (count > 0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean isItemInOrder(int orderId, int itemId){
        try {
            String sql = String.format("SELECT COUNT(*) FROM %s WHERE order_id = ? AND item_id = ?", ORDERS_ITEMS_TABLE);
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, orderId, itemId);
            return ((count != null) && (count > 0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int findItemQuantityInOrder(int orderId, int itemId) {
        try {
            String sql = String.format("SELECT quantity_in_order FROM %s WHERE order_id = ? AND item_id = ?", ORDERS_ITEMS_TABLE);
            return jdbcTemplate.queryForObject(sql, Integer.class, orderId, itemId);
        } catch (Exception e) {
            return 0;
        }
    }

    public String addItemToOrder(int orderId, int itemId, int quantityInOrder) {
        try {
            String sql = String.format("INSERT INTO %s (order_id, item_id, quantity_in_order) VALUES (?,?,?)", ORDERS_ITEMS_TABLE);
            jdbcTemplate.update(sql, orderId, itemId, quantityInOrder);
            return "Item successfully added to your order";
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String updateItemQuantityInOrder(int orderId, int itemId, int updatedQuantityInOrder) {
        try {
            String sql = String.format("UPDATE %s SET quantity_in_order = ? WHERE order_id = ? AND item_id = ?", ORDERS_ITEMS_TABLE);
            jdbcTemplate.update(sql, updatedQuantityInOrder, orderId, itemId);
            return "Item quantity in your order successfully updated";
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String removeItemFromOrder(int orderId, int itemId) {
        try {
            String sql = String.format("DELETE FROM %s WHERE order_id = ? AND item_id =?", ORDERS_ITEMS_TABLE);
            jdbcTemplate.update(sql, orderId, itemId);
            return "Item successfully removed from your order";
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String closeOpenOrder(String username) {
        try {
            String sql = String.format("UPDATE %s SET order_status = ? WHERE username = ? AND order_status = ?", ORDERS_TABLE);
            jdbcTemplate.update(sql, Status.CLOSE.name(), username, Status.OPEN.name());
            return "Order successfully closed";
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String deleteEmptyOrder(String username, int orderId){
        try {
            String sql = String.format("DELETE FROM %s WHERE username = ? AND order_id = ?", ORDERS_TABLE);
            jdbcTemplate.update(sql, username, orderId);
            return "Order successfully deleted";
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Order findOpenUserOrder(String username) {
        try {
            String sql = String.format("SELECT * FROM %s WHERE username = ? AND order_status = ?", ORDERS_TABLE);
            return jdbcTemplate.queryForObject(sql, new OrderMapper(), username, Status.OPEN.name());
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Item> findOrderItems(int orderId) {
        try {
            String sql = String.format("SELECT i.item_id, i.title, i.image_path, i.price, oi.quantity_in_order as quantity FROM %s oi JOIN %s i ON oi.item_id = i.item_id WHERE oi.order_id = ?", ORDERS_ITEMS_TABLE, ITEMS_TABLE);
            return jdbcTemplate.query(sql, new ItemMapper(), orderId);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean isOrderExist(int orderId) {
        try {
            String sql = String.format("SELECT COUNT(*) FROM %s WHERE order_id = ?", ORDERS_TABLE);
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, orderId);
            return (count != null && count > 0);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<Order> findAllClosedUserOrders(String username) {
        try {
            String sql = String.format("SELECT * FROM %s WHERE username = ? AND order_status = ?", ORDERS_TABLE);
            return jdbcTemplate.query(sql, new OrderMapper(), username, Status.CLOSE.name());
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}