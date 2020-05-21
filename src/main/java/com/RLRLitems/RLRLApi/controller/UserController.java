package com.RLRLitems.RLRLApi.controller;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.RLRLitems.RLRLApi.entity.Credentials;
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

}
