package com.example.security.repository.mapper;

import com.example.security.model.FavoriteItemResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class FavoriteItemResponseMapper implements RowMapper<FavoriteItemResponse> {
    @Override
    public FavoriteItemResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FavoriteItemResponse(
                rs.getInt("item_id"),
                rs.getString("title")
        );
    }
}
