package com.example.security.repository.mapper;

import com.example.security.model.Item;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Item(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("image_path"),
                rs.getInt("price"),
                rs.getInt("quantity")
        );
    }
}
