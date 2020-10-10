
package com.br.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.entity.User;

import com.br.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping
	public List<User> viewAllUsers() {
		return service.viewAllUsers();
	}

	@PostMapping
	public User createUser(@RequestBody User user) {
		return service.createUser(user);
	}

	@GetMapping("/{id}")
	public User viewUser(@PathVariable Integer id) {
		return service.viewUser(id);
	}

	@PutMapping("/{id}")
	public User updateUser(@PathVariable Integer id, @RequestBody User user) {
		user.setId(id);
		return service.updateUser(user);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Integer id) {
		service.deleteUser(id);
	}

}
