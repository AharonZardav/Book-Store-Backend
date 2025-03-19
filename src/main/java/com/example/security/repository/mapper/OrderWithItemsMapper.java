package com.example.security.repository.mapper;

import com.example.security.model.enums.Status;
import com.example.security.model.item.Item;
import com.example.security.model.order.Order;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderWithItemsMapper implements ResultSetExtractor<List<Order>> {
    public List<Order> extractData(ResultSet rs) throws SQLException {
        Map<Integer, Order> orderMap = new HashMap<>();

        while (rs.next()) {
            int orderId = rs.getInt("order_id");

            Order order = orderMap.get(orderId);
            if (order == null) {
                order = new Order(
                        orderId,
                        rs.getString("username"),
                        rs.getDate("order_date").toLocalDate(),
                        rs.getString("shipping_address"),
                        Status.valueOf(rs.getString("order_status"))
                );
                order.setItems(new ArrayList<>());
                orderMap.put(orderId, order);
            }

            int itemId = rs.getInt("item_id");
            if (itemId > 0) {
                Item item = new Item(
                        itemId,
                        rs.getString("title"),
                        rs.getString("image_path"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                );
                order.getItems().add(item);
            }
        }
        return new ArrayList<>(orderMap.values());

//        Order order = null;
//        List<Item> items = new ArrayList<>();
//
//        while (rs.next()){
//            if (order == null) {
//                order = new Order(
//                        rs.getInt("order_id"),
//                        rs.getString("username"),
//                        rs.getDate("order_date").toLocalDate(),
//                        rs.getString("shipping_address"),
//                        Status.valueOf(rs.getString("order_status"))
//                );
//                order.setItems(new ArrayList<>());
//            }
//
//            Item item = new Item(
//                    rs.getInt("item_id"),
//                    rs.getString("title"),
//                    rs.getString("image_path"),
//                    rs.getDouble("price"),
//                    rs.getInt("quantity")
//            );
//            order.getItems().add(item);
//        }
//
//        return order;
    }
}
