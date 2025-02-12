package com.example.security.repository.mapper;

import com.example.security.model.Order;
import com.example.security.model.Status;
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
