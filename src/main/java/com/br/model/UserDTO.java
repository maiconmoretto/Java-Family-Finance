package com.br.model;

import javax.persistence.Column;
import javax.persistence.Id;


public class UserDTO {

	@Column(name = "name")
	String name;
	@Column(name = "email")
	String email;

	public UserDTO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserDTO(String name, String email) {
		this.name = name;
		this.email = email;
	}
	
	public User changeToObject(){
	    return new User(name, email);
	}
}
