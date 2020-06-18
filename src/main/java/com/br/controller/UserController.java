
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

import com.br.model.User;
import com.br.model.UserDTO;
import com.br.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API User")
@RequestMapping("/api/v1/user/")
public class UserController {
	private final UserService service;

	@Autowired
	public UserController(UserService service) {
		this.service = service;
	}

	@ApiOperation(value = "It will return list of User")
	@GetMapping
	public @ResponseBody Iterable<User> getAllUsers() {
		return service.findAll();
	}

	@ApiOperation(value = "It will get a User by Id")
	@GetMapping("/{id}")
	public User findById(@PathVariable int id) {
		return service.findById(id);
	}
//
//	@ApiOperation(value = "It will add new User")
//	@PostMapping(path = "/api/v1/user/")
//	public ResponseEntity<User> save(@RequestBody User user) {
//		User userSaved = service.save(user);
//		return new ResponseEntity<>( service.save(user), HttpStatus.CREATED);
//	}
//	
	
	@ApiOperation(value = "It will add new User")
	@PostMapping
	public ResponseEntity<User> save(@RequestBody UserDTO dto) {
	    User user = service.save(dto.changeToObject());
	    return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@ApiOperation(value = "It will update User")
	@PutMapping(value = "/{id}")
	public ResponseEntity<User> update(@RequestBody User user) {
		User userSaved = service.update(user);
		return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
	}

	@ApiOperation(value = "It will delete User")
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		 service.deleteById(id);
		 return new ResponseEntity<>("User deleted", HttpStatus.OK);
	}

}
