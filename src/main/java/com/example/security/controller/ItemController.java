package com.example.security.controller;

import com.example.security.model.item.Item;
import com.example.security.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@CrossOrigin(origins = "http://localhost:3000")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/by-title")
    public ResponseEntity<Item> getItemByTitle(@RequestParam String title){
        try {
            Item item = itemService.getItemByTitle(title);
            if (item == null){
                return new ResponseEntity("Item not found", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(item);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Item>> getAllItems(){
        try {
            List<Item> items = itemService.getAllItems();
            if (items == null || items.isEmpty()){
                return new ResponseEntity("No items found", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(items);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
