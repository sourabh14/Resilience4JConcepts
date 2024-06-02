package com.example.userservice.controller;

import com.example.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final WebClient webClient;
    public static final String BASE_URL = "http://localhost:8080";
    public static final String USER_SERVICE = "userService";

    @GetMapping("/orders")
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "circuitBreakerFallback")
    public ResponseEntity<Object> getOrders() {
        Object[] response = webClient.get()
                .uri(BASE_URL + "/orders")
                .retrieve()
                .bodyToMono(Object[].class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        assert response != null;
        List<Order> orders = Arrays.stream(response)
                .map(object -> mapper.convertValue(object, Order.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // fallback method is called when circuit is in open state
    public ResponseEntity<Object> circuitBreakerFallback(CallNotPermittedException e) {
        return new ResponseEntity<>("The upstream service (order-service) is currently unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

}
