package com.br.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.entity.User;
import com.br.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private  UserRepository repository;
	

	public List<User> viewAllUsers() {
		return repository.findAll();
	}


	public User createUser(User user) {
		return repository.saveAndFlush(user);
	}

	public User viewUser(Integer id) {
		return repository.findById(id).get();
	}

	public User updateUser(User user) {
		return repository.saveAndFlush(user);
	}

	public void deleteUser(Integer id) {
		repository.deleteById(id);
	}
}
