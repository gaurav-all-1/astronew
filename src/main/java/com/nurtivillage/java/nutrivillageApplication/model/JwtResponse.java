package com.nurtivillage.java.geonixApplication.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class JwtResponse implements Serializable {

//	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private String userName;
	private List<String> roles;
	public JwtResponse(String jwttoken,String userName,List<String> roles) {
		this.jwttoken = jwttoken;
		this.userName=userName;
		this.roles = roles;
	}
	
	public JwtResponse(String jwttoken) {
		this.jwttoken = jwttoken;
	}

	public String getToken() {
		return this.jwttoken;
	}
}