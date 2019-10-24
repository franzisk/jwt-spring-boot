package com.safeplace.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safeplace.service.UserService;

// Please: dont't put business rules on this layer, this is only to communicate with the Service layer and those rules should be put there, NOT here.

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController {

	@Autowired
	UserService userService;

	/**
	 * Retrieve logged in user through session token
	 * 
	 * @return User data
	 */
	@GetMapping(value = "/user-data")
	public ResponseEntity<?> retrieveUserData(HttpServletRequest request) {
		return this.userService.retrieveUserDataByToken(request);
	}

}
