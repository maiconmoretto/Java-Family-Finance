package com.br.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.entity.User;
import com.br.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository repository;
	
	@Autowired
	public UserService(UserRepository repository) {
		this.repository = repository;
	}

	public User save(User user) {
		return repository.save(user);
	}

	public Iterable<User> findAll() {
		return repository.findAll();
	}
	
	public User findById(int id) {
		return repository.findById(id).get();
	}	

	public User update(User user) {
		return repository.save(user);
	}

	public void deleteById(int id) {
		repository.deleteById(id);
	}
}
