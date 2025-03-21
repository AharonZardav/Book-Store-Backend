package com.example.security.service;

import com.example.security.model.item.Item;
import com.example.security.repository.FavoritesListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritesListService {

    @Autowired
    private FavoritesListRepository favoritesListRepository;

    @Autowired
    private ItemService itemService;

    public List<Item> getFavoritesList(String username){
        return favoritesListRepository.findFavoriteItems(username);
    }

    public String addItemToFavoriteList(String username, String itemTitle){
        Item item = itemService.getItemByTitle(itemTitle);
        if (itemTitle == null){
            return "Item not added: Item title is required";
        }
        if (item == null) {
            return "Item not added: Item with this title dose not exist";
        }
        if (favoritesListRepository.isItemIsFavorite(username, item.getItemId())){
            return "Item not added: The item already exists in your favorites list";
        }
        return favoritesListRepository.addItemToFavoriteList(username, item.getItemId());
    }

    public String removeItemFromFavoriteList(String username, String itemTitle){
        Item item = itemService.getItemByTitle(itemTitle);
        if (itemTitle == null){
            return "Item not removed: Item title is required";
        }
        if (item == null) {
            return "Item not removed: Item with this title dose not exist";
        }
        if (!favoritesListRepository.isItemIsFavorite(username, item.getItemId())){
            return "Item not removed: You cannot remove an item that does not exist in your favorites list";
        }
        return favoritesListRepository.removeItemFromFavoriteList(username, item.getItemId());
    }

    public String removeAllItemsFromFavoriteList(String username){
//        List<Item> favoriteItemList = favoritesListRepository.findFavoriteItems(username);
//        if (favoriteItemList == null || favoriteItemList.isEmpty()){
//            return "Favorites list not updated: Your Favorites list is already empty";
//        }
        return favoritesListRepository.removeAllItemsFromFavoriteList(username);
    }
}
