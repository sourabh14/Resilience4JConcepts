package com.example.orderservice.controller;

import com.example.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    @GetMapping()
    public ResponseEntity<List<Order>> getOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("1", "Order1", 200));
        orders.add(new Order("2", "Order2", 200));
        orders.add(new Order("3", "Order3", 200));
        orders.add(new Order("4", "Order4", 200));
        orders.add(new Order("5", "Order5", 200));
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
