
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API User")
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService service;

	@ApiOperation(value = "It will return list of User")
	@GetMapping
	public List<User> viewAllUsers() {
		return service.viewAllUsers();
	}

	@ApiOperation(value = "It will add new User")
	@PostMapping
	public User createUser(@RequestBody User user) {
		return service.createUser(user);
	}

	@ApiOperation(value = "It will get a User by Id")
	@GetMapping("/{id}")
	public User viewUser(@PathVariable Integer id) {
		return service.viewUser(id);
	}

	@ApiOperation(value = "It will update User")
	@PutMapping("/{id}")
	public User updateUser(@PathVariable Integer id, @RequestBody User user) {
		user.setId(id);
		return service.updateUser(user);
	}

	@ApiOperation(value = "It will delete User")
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Integer id) {
		service.deleteUser(id);
	}

}
