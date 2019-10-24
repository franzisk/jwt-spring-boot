package com.safeplace.exception;

public class AuthenticationException extends RuntimeException {
	private static final long serialVersionUID = -1588911577343277851L;

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}
