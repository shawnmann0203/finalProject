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

import com.RLRLitems.RLRLApi.service.TeamService;
import com.RLRLitems.RLRLApi.entity.Product;
import com.RLRLitems.RLRLApi.entity.Team;
import com.RLRLitems.RLRLApi.service.AuthService;
import com.RLRLitems.RLRLApi.service.ProductService;

@RestController
@RequestMapping("/teams")
public class TeamController {
	
	@Autowired
	private TeamService service;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private ProductService productService;
	
	//localhost:8080/teams
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Object> getTeams(){
		return new ResponseEntity<Object>(service.getTeams(), HttpStatus.OK);
	}
	
	//localhost:8080/teams/{id}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getTeam(@PathVariable Long id){
		return new ResponseEntity<Object>(service.getTeam(id), HttpStatus.OK);
	}
	
	//localhost:8080/teams/{id}
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> createTeam(@RequestBody Team team, HttpServletRequest request) throws Exception{
			if(authService.isAdmin(authService.getToken(request))) {
				return new ResponseEntity<Object>(service.createTeam(team), HttpStatus.CREATED);
			} else {
				return new ResponseEntity<Object>("Unauthorized request.", HttpStatus.UNAUTHORIZED);
			}
		
	}
	
	//localhost:8080/teams/{id}
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteTeam(@PathVariable Long id, HttpServletRequest request) throws Exception{
		try {
			if(authService.isAdmin(authService.getToken(request))) {
				service.removeTeam(id);
				return new ResponseEntity<Object>("Successfully deleted Team with id: " + id, HttpStatus.OK);
			}else {
				return new ResponseEntity<Object>("Unauthorized request.", HttpStatus.UNAUTHORIZED);
			}
			}catch(Exception e) {
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
		
	}
	
	//localhost:8080/teams/{teamId}/products
	@RequestMapping(value = "/{teamId}/products", method = RequestMethod.POST)
	public ResponseEntity<Object> createTeamProduct(@RequestBody Product product, @PathVariable Long teamId, HttpServletRequest request) throws Exception{
		try {
			if(authService.isAdmin(authService.getToken(request))) {
				return new ResponseEntity<Object>(productService.createProduct(product, teamId), HttpStatus.CREATED);
			}else {
				return new ResponseEntity<Object>("Unauthorized request.", HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	//localhost:8080/products/team/{teamId}
	@RequestMapping(value = "/{teamId}/products", method = RequestMethod.GET)
	public ResponseEntity<Object> getProductByTeamId(@PathVariable Long teamId){
		return new ResponseEntity<Object>(productService.getProductsByTeamId(teamId), HttpStatus.OK);
			
	}
	
	

}

