
package com.br.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.model.User;
import com.br.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API User")
public class UserController {
	@Autowired
	private UserService service;

	@ApiOperation(value = "It will return list of User")
	@GetMapping(path = "/api/v1/user/")
	public @ResponseBody Iterable<User> getAllUsers() {
		return service.findAll();
	}

	@ApiOperation(value = "It will get a User by Id")
	@GetMapping("/api/v1/user/{id}")
	public User findById(@PathVariable int id) {
		return service.findById(id);
	}

	@ApiOperation(value = "It will add new User")
	@PostMapping(path = "/api/v1/user/")
	public @ResponseBody ResponseEntity save(@RequestBody User expense) {
		return service.save(expense);
	}

	@ApiOperation(value = "It will update User")
	@PutMapping(value = "/api/v1/user/{id}")
	public ResponseEntity<String> update(@RequestBody User expense) {
		return service.update(expense);
	}

	@ApiOperation(value = "It will delete User")
	@DeleteMapping(path = "/api/v1/user/{id}")
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		return service.deleteById(id);
	}

}