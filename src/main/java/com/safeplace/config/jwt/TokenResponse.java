package com.safeplace.config.jwt;

import java.io.Serializable;
import java.util.Date;

public class TokenResponse implements Serializable {

	private static final long serialVersionUID = 8317676219297719109L;

	private final String token;
	private final Date expiration;

	public TokenResponse(String token, Date expiration) {
		this.token = token;
		this.expiration = expiration;
	}

	public String getToken() {
		return this.token;
	}

	public Long getDateTimeExpiration() {
		return this.expiration.getTime();
	}
}