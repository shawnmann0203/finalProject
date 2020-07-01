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

import com.RLRLitems.RLRLApi.entity.Credentials;
import com.RLRLitems.RLRLApi.entity.User;
import com.RLRLitems.RLRLApi.service.AuthService;
import com.RLRLitems.RLRLApi.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@Autowired
	private AuthService authService;
		
	
	//localhost:8080/user/register
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Object> register(@RequestBody Credentials cred){
		try {
			return new ResponseEntity<Object>(authService.register(cred, "USER"), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	//localhost:8080/user/adminregister
	@RequestMapping(value = "/adminregister", method = RequestMethod.POST)
	public ResponseEntity<Object> adminRegister(@RequestBody Credentials cred, HttpServletRequest request){
		try {
			if(authService.isAdmin(authService.getToken(request))) {
				return new ResponseEntity<Object>(authService.register(cred, "ADMIN"), HttpStatus.CREATED);
			} else {
				return new ResponseEntity<Object>("Unauthorized request.", HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	//localhost:8080/user/login
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Object> login(@RequestBody Credentials cred){
		try {
			return new ResponseEntity<Object>(authService.login(cred), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	//localhost:8080/user/{id}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getUserInfo(@PathVariable Long id, HttpServletRequest request){
		try {
		if(authService.isCorrectUser(authService.getToken(request), id) || authService.isAdmin(authService.getToken(request))) {
			return new ResponseEntity<Object>(service.getUser(id), HttpStatus.OK);
		}else {
			return new ResponseEntity<Object>("Unauthorized request.", HttpStatus.UNAUTHORIZED);
		}
		}catch(Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	//localhost:8080/user/{id}
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteUser(@PathVariable Long id, HttpServletRequest request){
		try {
			if(authService.isCorrectUser(authService.getToken(request), id) || authService.isAdmin(authService.getToken(request))) {
				service.deleteUser(id);
				return new ResponseEntity<Object>("Successfully deleted User with id: " + id, HttpStatus.OK);
			}else {
				return new ResponseEntity<Object>("Unauthorized request.", HttpStatus.UNAUTHORIZED);
			}
			}catch(Exception e) {
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
	}
	
	//localhost:8080/user/{id}
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateUserInfo(@RequestBody User user, @PathVariable Long id, HttpServletRequest request){
		try {
			if(authService.isCorrectUser(authService.getToken(request), id)) {
				return new ResponseEntity<Object>(service.updateUserInfo(user, id), HttpStatus.OK);
			}else {
				return new ResponseEntity<Object>("Unauthorized request.", HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	
	

}
