package com.example.security.service;

import com.example.security.model.item.Item;
import com.example.security.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Item getItemByTitle(String title){
        return itemRepository.findItemByTitle(title);
    }

    public Item getItemById(int itemId){
        return itemRepository.findItemById(itemId);
    }

//    public int findItemQuantityInStock(int itemId){
//        Item item = itemRepository.findItemQuantityInStock(itemId);
//        return item.getQuantity();
//    }

    public List<Item> getAllItems(){
        return itemRepository.findAllItems();
    }

    public String buyItem(int itemId, int quantityToReduce){
        if (quantityToReduce <= 0){
            return "Item purchase failed: Quantity must be at least 1";
        }
        Item item = getItemById(itemId);
        if (item == null){
            return "Item purchase failed: Item dose not exist";
        }
        int quantityInStock = item.getQuantity();
        if (quantityInStock < quantityToReduce){
            return "Item purchase failed: Not enough items in stock";
        }

        int updatedItemQuantity = quantityInStock - quantityToReduce;
        return itemRepository.updateItemQuantityInStock(updatedItemQuantity, itemId);
    }
}