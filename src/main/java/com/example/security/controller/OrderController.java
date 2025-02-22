package com.example.security.controller;

import com.example.security.model.item.ItemRequest;
import com.example.security.model.order.OrderList;
import com.example.security.service.ItemService;
import com.example.security.service.OrderService;
import com.example.security.service.UserService;
import com.example.security.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(value = "/addItem")
    public ResponseEntity<String> addItemToOrder(@RequestHeader(value = "Authorization") String token, @RequestBody ItemRequest itemRequest){
        try {
            String jwtToken = token.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);

            //checks if the order sent belongs to the authorized user
            if (!username.equals(itemRequest.getUsername())){
                return new ResponseEntity("You are not allowed to access this function", HttpStatus.UNAUTHORIZED);
            }

            String result = orderService.addItemToOrder(itemRequest.getUsername(), itemRequest.getTitle(), itemRequest.getQuantity(), itemRequest.getShippingAddress());
            if (result.contains("successfully")) {
                return new ResponseEntity(result, HttpStatus.CREATED);
            }
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping(value = "/removeItem")
    public ResponseEntity<String> removeItemFromOrder(@RequestHeader(value = "Authorization") String token, @RequestBody ItemRequest itemRequest){
        try {
            String jwtToken = token.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);

            //checks if the order sent belongs to the authorized user
            if (!username.equals(itemRequest.getUsername())){
                return new ResponseEntity("You are not allowed to access this function", HttpStatus.UNAUTHORIZED);
            }

            String result = orderService.removeItemFromOrder(itemRequest.getUsername(), itemRequest.getTitle(), itemRequest.getQuantity());
            if (result.contains("successfully")) {
                return new ResponseEntity(result, HttpStatus.OK);
            }
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(value = "/payment")
    public ResponseEntity<String> closeOpenOrder(@RequestHeader(value = "Authorization") String token){
        try {
            String jwtToken = token.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            String result = orderService.closeOrder(username);
            if (result.contains("successfully")) {
                return new ResponseEntity(result+". Your purchase was made successfully ", HttpStatus.OK);
            }
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(value = "/all")
    public ResponseEntity<OrderList> getAllOrders(@RequestHeader(value = "Authorization") String token){
        try {
            String jwtToken = token.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);
            OrderList orderList= orderService.getAllUserOrders(username);
            if (orderList == null) {
                return new ResponseEntity(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(orderList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
