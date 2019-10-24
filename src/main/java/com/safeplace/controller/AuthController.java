package com.safeplace.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.safeplace.config.jwt.TokenRequest;
import com.safeplace.exception.AuthenticationException;
import com.safeplace.service.AuthService;

@RestController
@CrossOrigin
public class AuthController {

	@Autowired
	AuthService authService;

	// Generates the initial token
	// Call: http://localhost:8086/auth/login
	@PostMapping(value = "${jwt.get.token.uri}")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody TokenRequest authenticationRequest) throws AuthenticationException {
		return this.authService.createAuthenticationToken(authenticationRequest);
	}

	// Refreshs the current token
	@GetMapping(value = "${jwt.refresh.token.uri}")
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		return this.authService.refreshAndGetAuthenticationToken(request);
	}

}
