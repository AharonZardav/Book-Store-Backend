package com.example.security.repository.mapper;

import com.example.security.model.order.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemMapper implements RowMapper {
    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new OrderItem(
                rs.getInt("item_id"),
                rs.getInt("quantity_in_order")
        );
    }
}
