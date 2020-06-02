package com.RLRLitems.RLRLApi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.RLRLitems.RLRLApi.entity.Product;
import com.RLRLitems.RLRLApi.service.AuthService;
import com.RLRLitems.RLRLApi.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Object> getProducts(){
		return new ResponseEntity<Object>(service.getProducts(), HttpStatus.OK);
	}
	
	//localhost:8080/products
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> createProduct(@RequestBody Product product, @RequestBody Long teamId, HttpServletRequest request) throws Exception{
		if(authService.isAdmin(authService.getToken(request))) {
			return new ResponseEntity<Object>(service.createProduct(product, teamId), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<Object>("Unauthorized request.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	//localhost:8080/products/{id}
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> removeProduct(@PathVariable Long id, HttpServletRequest request) throws Exception{
		try {
			if(authService.isAdmin(authService.getToken(request))) {
				service.removeProduct(id);
				return new ResponseEntity<Object>("Successfully deleted Product with id: " + id, HttpStatus.OK);
			}else {
				return new ResponseEntity<Object>("Unauthorized request.", HttpStatus.UNAUTHORIZED);
			}
			}catch(Exception e) {
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
	}
	
	//localhost:8080/products/{id}
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> updateProduct(@RequestBody Product product, @PathVariable Long id, HttpServletRequest request) throws Exception{
		try {
			if(authService.isAdmin(authService.getToken(request))) {
				service.updateProduct(product, id);
				return new ResponseEntity<Object>("Successfully update Product with id: " + id, HttpStatus.OK);
			}else {
				return new ResponseEntity<Object>("Unauthorized request.", HttpStatus.UNAUTHORIZED);
			}
			}catch(Exception e) {
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
	}
	

	
	


}
