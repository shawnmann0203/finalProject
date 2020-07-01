package com.RLRLitems.RLRLApi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.RLRLitems.RLRLApi.entity.User;
import com.RLRLitems.RLRLApi.repository.UserRepository;

@Service
public class UserService {
	
	private static final Logger logger = LogManager.getLogger(ProductService.class);
	
	@Autowired
	private UserRepository repo;
	
	public User getUser(Long id) {
		return repo.findOne(id);
	}
	
	public User updateUserInfo(User user, Long id) throws Exception{
		try {
			User updateUser = repo.findOne(id);
			updateUser.setAddress(user.getAddress());
			updateUser.setFirstName(user.getFirstName());
			updateUser.setLastName(user.getLastName());
			updateUser.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
			updateUser.setAddress(user.getAddress());
			return repo.save(updateUser);
		}catch(Exception e) {
			logger.error("Exception occured while trying to update user: " + id, e);
			throw new Exception("Unable to update user.");
		}
	}
	
	public void deleteUser(Long id) throws Exception {
		try {
			repo.delete(id);
		}catch(Exception e) {
			logger.error("Exception occured while trying to delete user: " + id, e);
			throw new Exception("Unable to delete user.");
		}
		
	}
	
	public void addGuestUser(User guest) {
		repo.save(guest);
	}
	
}
