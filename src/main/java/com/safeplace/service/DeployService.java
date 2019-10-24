package com.safeplace.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safeplace.model.User;
import com.safeplace.repository.UserRepository;
import com.safeplace.util.Util;

@Service
public class DeployService {

	@Autowired
	Logger logger;

	@Autowired
	UserRepository userRepository;

	PasswordEncoder passwordEncoder = Util.passwordEncoder();

	@Transactional
	private void insertFirstUser() {
		User user = new User();
		user.setAge(20);
		user.setFullName("John Ping Doe");
		user.setId(1L);
		user.setPassword(this.passwordEncoder.encode("ThePassword@987"));
		user.setUsername("secure.user@websitecompany.com"); // threat this as an email address
		user = this.userRepository.save(user);
	}

	@Transactional
	public void afterDeployAction() {
		logger.info(">> Deploy finished at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		long totalUsers = this.userRepository.count();
		if (totalUsers < 1) {
			logger.info(">> Inserting the first user for you...");
			this.insertFirstUser();
		}
	}
}
