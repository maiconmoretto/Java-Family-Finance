package com.br.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.br.model.User;
import com.br.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository repository;

	public Iterable<User> findAll() {
		return repository.findAll();
	}
	
	public User findById(int id) {
		return repository.findById(id).get();
	}	

	public ResponseEntity<String> save(User expense) {
		repository.save(expense);
		return new ResponseEntity<>("User successfully registered", HttpStatus.CREATED);
	}

	public ResponseEntity<String> update(User expense) {
		Optional<User> expenseExist = repository.findById(expense.getId());
		if (!expenseExist.isPresent()) {
			return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
		}
		repository.save(expense);
		return new ResponseEntity<>("User successfully updated", HttpStatus.OK);
	}

	public ResponseEntity<String> deleteById(int id) {
		repository.deleteById(id);
		return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
	}
}
