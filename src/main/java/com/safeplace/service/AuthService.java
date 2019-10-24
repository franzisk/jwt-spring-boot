package com.safeplace.service;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;

import com.safeplace.config.jwt.JwtUserDetails;
import com.safeplace.config.jwt.TokenRequest;
import com.safeplace.config.jwt.TokenResponse;
import com.safeplace.config.jwt.TokenUtil;
import com.safeplace.exception.AuthenticationException;
import com.safeplace.model.User;
import com.safeplace.repository.UserRepository;

@Service
public class AuthService implements UserDetailsService {

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	@Autowired
	private TokenUtil tokenUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	// Generates the token using username and password
	public ResponseEntity<?> createAuthenticationToken(@RequestBody TokenRequest authenticationRequest) throws AuthenticationException {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = this.loadUserByUsername(authenticationRequest.getUsername());
		final String token = tokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new TokenResponse(token, this.tokenUtil.getExpirationDateFromToken(token)));
	}

	// Refreshs the current token
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		if (tokenUtil.canTokenBeRefreshed(token)) {
			String refreshedToken = tokenUtil.refreshToken(token);
			return ResponseEntity.ok(new TokenResponse(refreshedToken, this.tokenUtil.getExpirationDateFromToken(refreshedToken)));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	// Get user by username
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> oUser = this.userRepository.findByUsername(username);
		if (!oUser.isPresent()) {
			throw new UsernameNotFoundException(String.format("USER_NOT_FOUND:'%s'.", username));
		} else {
			User user = oUser.get();
			String password = user.getPassword();
			JwtUserDetails userDetails = new JwtUserDetails(user.getId(), username, password, "ROLE_USER_2");
			return userDetails;
		}
	}

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	private void authenticate(String username, String password) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("INVALID_CREDENTIALS", e);
		}
	}

}
