package com.example.security.repository;

import com.example.security.model.item.Item;
import com.example.security.repository.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String ITEMS_TABLE = "items";
    
    public Item findItemByTitle(String title) {
        try {
            String sql = String.format("SELECT * FROM %s WHERE title = ?", ITEMS_TABLE);
            return jdbcTemplate.queryForObject(sql, new ItemMapper(),title);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Item findItemById(int itemId) {
        try {
            String sql = String.format("SELECT * FROM %s WHERE item_id = ?", ITEMS_TABLE);
            return jdbcTemplate.queryForObject(sql, new ItemMapper(),itemId);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

//    public Item findItemQuantityInStock(int itemId) {
//        try {
//            String sql = String.format("SELECT quantity FROM %s WHERE item_id = ?", ITEMS_TABLE);
//            return jdbcTemplate.queryForObject(sql, new ItemMapper(),itemId);
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//            return null;
//        }
//    }

    public List<Item> findAllItems() {
        String sql = String.format("SELECT * FROM %s", ITEMS_TABLE);
        List<Item> items = jdbcTemplate.query(sql, new ItemMapper());
        return items;
    }

    public String updateItemQuantityInStock(int updatedItemQuantity, int itemId){
        String sql = String.format("UPDATE %s SET quantity = ? WHERE item_id = ?", ITEMS_TABLE);
        jdbcTemplate.update(sql, updatedItemQuantity, itemId);
        return "Quantity updated successfully: "+updatedItemQuantity+" items left in stock";
    }
}
