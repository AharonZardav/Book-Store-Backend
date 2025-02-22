package com.example.security.repository;

import com.example.security.model.FavoriteItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FavoritesListRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String FAVORITES_LIST_TABLE = "favorites_list";

    public List<Integer> findFavoriteItems(String username){
        try {
            String sql = String.format("SELECT item_id FROM %s WHERE username = ?", FAVORITES_LIST_TABLE);
            return jdbcTemplate.queryForList(sql, Integer.class, username);
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
