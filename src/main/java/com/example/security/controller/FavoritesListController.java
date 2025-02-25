package com.example.security.controller;

import com.example.security.model.FavoriteItemResponse;
import com.example.security.model.ItemTitleRequest;
import com.example.security.service.FavoritesListService;
import com.example.security.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite-list")
@CrossOrigin(origins = "http://localhost:3000")
public class FavoritesListController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FavoritesListService favoritesListService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(value = "/all")
    public ResponseEntity<List<FavoriteItemResponse>> getFavoritesList(@RequestHeader(value = "Authorization") String token){
        try {
            String jwtToken = token.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);

            List<FavoriteItemResponse> favoriteItemsList= favoritesListService.getFavoritesList(username);
            if (favoriteItemsList == null) {
                return new ResponseEntity(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(favoriteItemsList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(value = "/add-item")
    public ResponseEntity<String> addItemToFavoriteList(@RequestHeader(value = "Authorization") String token, @RequestBody ItemTitleRequest itemTitleRequest){
        try {
            String jwtToken = token.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);

            String result = favoritesListService.addItemToFavoriteList(username,itemTitleRequest.getItem_title());
            if (result.contains("successfully")) {
                return new ResponseEntity(result, HttpStatus.CREATED);
            }
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping(value = "/remove-item")
    public ResponseEntity<String> removeItemFromOrder(@RequestHeader(value = "Authorization") String token, @RequestBody ItemTitleRequest itemTitleRequest){
        try {
            String jwtToken = token.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);

            String result = favoritesListService.removeItemFromFavoriteList(username,itemTitleRequest.getItem_title());
            if (result.contains("successfully")) {
                return new ResponseEntity(result, HttpStatus.OK);
            }
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping(value = "/remove-all")
    public ResponseEntity<List<FavoriteItemResponse>> removeAllItemsFromFavoriteList(@RequestHeader(value = "Authorization") String token){
        try {
            String jwtToken = token.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);

            String result = favoritesListService.removeAllItemsFromFavoriteList(username);
            if (result.contains("successfully")) {
                return new ResponseEntity(result, HttpStatus.OK);
            }
            return new ResponseEntity(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
