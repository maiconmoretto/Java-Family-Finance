
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

import com.br.repository.UserRepository;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public List<User> viewAllUsers() {
		return userRepository.findAll();
	}

	@PostMapping
	public User createUser(@RequestBody User user) {
		return userRepository.saveAndFlush(user);
	}

	@GetMapping("/{id}")
	public User viewUser(@PathVariable Integer id) {
		return userRepository.findById(id).get();
	}

	@PutMapping("/{id}")
	public User updateUser(@PathVariable Integer id, @RequestBody User user) {
		user.setId(id);

		return userRepository.saveAndFlush(user);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Integer id) {
		userRepository.deleteById(id);
	}


}
