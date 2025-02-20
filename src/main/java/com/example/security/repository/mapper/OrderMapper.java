package com.example.security.repository.mapper;

import com.example.security.model.order.Order;
import com.example.security.model.enums.Status;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Order(
                rs.getInt("order_id"),
                rs.getString("username"),
                rs.getDate("order_date").toLocalDate(),
                rs.getString("shipping_address"),
                Status.valueOf(rs.getString("order_status"))
        );
    }
}
