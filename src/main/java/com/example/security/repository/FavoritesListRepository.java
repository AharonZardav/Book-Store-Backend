package com.example.security.repository;

import com.example.security.model.item.Item;
import com.example.security.repository.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FavoritesListRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String FAVORITES_LIST_TABLE = "favorites_list";
    private static final String ITEMS_TABLE = "items";

    public List<Item> findFavoriteItems(String username){
        try {
            String sql = String.format("SELECT f.item_id, i.* FROM %s f JOIN %s i ON f.item_id = i.item_id where f.username = ?", FAVORITES_LIST_TABLE, ITEMS_TABLE);
            return jdbcTemplate.query(sql, new ItemMapper(), username);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean isItemIsFavorite(int itemId){
        try {
            String sql = String.format("SELECT COUNT (*) FROM %s WHERE item_id = ?", FAVORITES_LIST_TABLE);
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, itemId);
            return (count != null && count > 0);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String addItemToFavoriteList(String username, int item_id){
        try {
            String sql = String.format("INSERT INTO %s (username, item_id) VALUES (?,?)", FAVORITES_LIST_TABLE);
            jdbcTemplate.update(sql, username, item_id);
            return "Item added to Favorites List successfully";
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String removeItemFromFavoriteList(String username, int itemId){
        try {
            String sql = String.format("DELETE FROM %s WHERE username = ? AND item_id = ?", FAVORITES_LIST_TABLE);
            jdbcTemplate.update(sql, username, itemId);
            return "Item removed from Favorites List successfully";
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String removeAllItemsFromFavoriteList(String username){
        try {
            String sql = String.format("DELETE FROM %s WHERE username = ?", FAVORITES_LIST_TABLE);
            jdbcTemplate.update(sql, username);
            return "Favorites list has been successfully emptied";
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
