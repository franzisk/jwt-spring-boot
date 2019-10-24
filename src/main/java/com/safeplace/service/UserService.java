package com.safeplace.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.safeplace.config.jwt.TokenUtil;
import com.safeplace.model.User;
import com.safeplace.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	TokenUtil tokenUtil;

	// Retrieve logged in user through session token
	public ResponseEntity<?> retrieveUserDataByToken(HttpServletRequest request) {
		return new ResponseEntity<>(getUserByToken(request), HttpStatus.OK);
	}

	private User getUserByToken(HttpServletRequest request) {
		String token = this.tokenUtil.getTokenFromRequest(request);
		String username = this.tokenUtil.getUsernameFromToken(token);
		User user = this.userRepository.findByUsername(username).get();
		return user;
	}
}
