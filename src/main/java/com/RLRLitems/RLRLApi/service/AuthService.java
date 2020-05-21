package com.RLRLitems.RLRLApi.service;

import java.security.Key;

import javax.naming.AuthenticationException;

import javax.servlet.http.HttpServletRequest;

import javax.mail.internet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.RLRLitems.RLRLApi.entity.Credentials;
import com.RLRLitems.RLRLApi.entity.User;
import com.RLRLitems.RLRLApi.repository.UserRepository;
import com.RLRLitems.RLRLApi.util.MemberRank;
import com.RLRLitems.RLRLApi.view.LoggedInView;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Service
public class AuthService {
	
	@Autowired
	private UserRepository userRepository;
	

	
	private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	public User register(Credentials cred, String role) throws AuthenticationException {
		User user = new User();
		user.setUsername(cred.getUsername());
		user.setHash(BCrypt.hashpw(cred.getPassword(), BCrypt.gensalt()));
		user.setEmail(cred.getEmail());
		user.setRole(role);
		user.setRank(MemberRank.BRONZE);
		if(!isValidEmailAddress(cred.getEmail())){
			throw new AuthenticationException("Email is not valid");
		} else {
			try {
				userRepository.save(user);
				return user;
			} catch (DataIntegrityViolationException e){
				throw new AuthenticationException("Username or Email already in use.");
			}
		}
	}
	
	
	public LoggedInView login(Credentials cred) throws AuthenticationException {
		User foundUser = userRepository.findByUsername(cred.getUsername());
		if(foundUser != null && BCrypt.checkpw(cred.getPassword(), foundUser.getHash())) {
			LoggedInView view = new LoggedInView();
			view.setUser(foundUser);
			view.setJwt(generateToken(foundUser));
		}
		throw new AuthenticationException("Incorrect username or password.");
	}
	
	private String generateToken(User user) {
		String jwt = Jwts.builder()
				.claim("role", user.getRole())
				.claim("userId", user.getId())
				.setSubject("RL RL ITEMS")
				.signWith(key)
				.compact();
		return jwt;
	}
	
	public boolean isAdmin(String token) {
		return ((String)Jwts.parser()
				.setSigningKey(key)
				.parseClaimsJws(token)
				.getBody()
				.get("role"))
				.equals("ADMIN");
	}
	
	public boolean isCorrectUser(String jwt, Long userId) {
		return new Long((Integer)Jwts.parser()
				.setSigningKey(key)
				.parseClaimsJws(jwt)
				.getBody()
				.get("userId"))
				.equals(userId);
	}
	
	public String getToken(HttpServletRequest request) throws Exception {
		String header = request.getHeader("Authorization");
		if (header == null) {
			throw new Exception("Request contains no token.");
		}
		return header.replaceAll("Bearer ", "");
	}
	
	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		 try {
		    InternetAddress emailAddr = new InternetAddress(email);
		    emailAddr.validate();
		 } catch (AddressException ex) {
		    result = false;
		 }
		 return result;
	}

}
