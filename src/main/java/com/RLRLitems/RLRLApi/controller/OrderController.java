package com.RLRLitems.RLRLApi.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.RLRLitems.RLRLApi.entity.Order;
import com.RLRLitems.RLRLApi.entity.User;
import com.RLRLitems.RLRLApi.service.AuthService;
import com.RLRLitems.RLRLApi.service.OrderService;
import com.RLRLitems.RLRLApi.util.OrderStatus;


@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderService service;
	
	@Autowired
	private AuthService authService;
	
	//localhost:8080/orders/user/{id}
	@RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> createUserOrder(@RequestBody Set<Long> productIds, @PathVariable Long id, HttpServletRequest request){
		try {
			if(authService.isCorrectUser(authService.getToken(request), id)) {
				return new ResponseEntity<Object>(service.submitNewOrder(productIds, id), HttpStatus.CREATED);
			} else {
				return new ResponseEntity<Object>("Unauthorized request.", HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	//localhost:8080/orders/{id}
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateOrder(@RequestBody Order order, @PathVariable Long id, HttpServletRequest request){
		try {
			if(authService.isAdmin(authService.getToken(request))) {
				if(order.getStatus().equals(OrderStatus.CANCELLED)) {
					return new ResponseEntity<Object>(service.cancelOrder(id), HttpStatus.OK);
				} else if (order.getStatus().equals(OrderStatus.DELIVERED)) {
					return new ResponseEntity<Object>(service.deliveredOrder(id), HttpStatus.OK);
				} else {
					return new ResponseEntity<Object>("Invalid update request.", HttpStatus.BAD_REQUEST);
				}
			}else {
				return new ResponseEntity<Object>("Unauthorized request.", HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	//localhost:8080/orders/guest/checkout
	@RequestMapping(value="/guest/checkout", method = RequestMethod.POST)
	public ResponseEntity<Object> createGuestOrder(@RequestBody Set<Long> productIds, @RequestBody User guest){
		try {
			return new ResponseEntity<Object>(service.submitNewGuestOrder(productIds, guest), HttpStatus.CREATED);
		} catch(Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	//localhost:8080/orders
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Object> getOrders(HttpServletRequest request) throws Exception{
		try {
			if(authService.isAdmin(authService.getToken(request))) {
				return new ResponseEntity<Object>(service.getOrders(), HttpStatus.OK);
			}else {
				return new ResponseEntity<Object>("Unauthorized request.", HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
