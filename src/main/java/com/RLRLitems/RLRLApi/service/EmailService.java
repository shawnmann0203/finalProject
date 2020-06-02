package com.RLRLitems.RLRLApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.RLRLitems.RLRLApi.component.EmailConfig;
import com.RLRLitems.RLRLApi.entity.Credentials;

@Service
public class EmailService {
	
	@Autowired
	private EmailConfig config;
	
	public void sendWelcomeEmail(Credentials cred) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(config.getHost());
		mailSender.setPort(config.getPort());
		mailSender.setUsername(config.getUsername());
		mailSender.setPassword(config.getPassword());
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("shawnmann0203@gmail.com");
		mailMessage.setTo(cred.getEmail());
		mailMessage.setSubject("Welcome to Real Life Rocket League Items!");
		mailMessage.setText("Hello, " 
							+ "\n\n Welcome to Real Life Rocket League Items! \n"
							+"A place where you can find all your rocket league gear in one place.\n"
							+ "Every $150 dollars spent with us, your rank goes up. Just like rocket league,"
							+ " you'll start at bronce and work your way up the ranks to grand champion,"
							+ " a whopping 15 percent discount.\n Don't forget your username and password information below."
							+ "\n Thank you for signing up and happy shopping."
							+ "\n\n Username: " + cred.getUsername() + "\nPassword: " + cred.getPassword());
		
		mailSender.send(mailMessage);
	}
	
	
	
	
	
}
