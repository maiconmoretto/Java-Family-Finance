package com.br.model;

import javax.persistence.Column;
import javax.persistence.Id;


public class UserDTO {

	@Id
	@Column(name = "id")
	int id;
	@Column(name = "name")
	String name;
	@Column(name = "email")
	String email;
	@Column(name = "created_at")
	String createdAt;

	public UserDTO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public UserDTO(String name, String createdAt, String email) {
		this.name = name;
		this.createdAt = createdAt;
		this.email = email;
	}
	
	public User changeToObject(){
	    return new User(name, createdAt, email);
	}
}
