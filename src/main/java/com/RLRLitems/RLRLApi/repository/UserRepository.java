package com.RLRLitems.RLRLApi.repository;

import org.springframework.data.repository.CrudRepository;

import com.RLRLitems.RLRLApi.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{
	
	
	public User findByUsername(String username);

}
