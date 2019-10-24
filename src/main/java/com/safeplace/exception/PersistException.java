package com.safeplace.exception;

public class PersistException extends RuntimeException {

	private static final long serialVersionUID = 7025870880098466550L;

	public PersistException(String message) {
		super(message);
	}
}