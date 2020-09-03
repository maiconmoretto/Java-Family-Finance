package com.br.service;

import com.br.model.UserDTO;
import feign.FeignException;
import com.br.user.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.br.model.Category;
import com.br.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryService {
	
	private final CategoryRepository repository;

	@Autowired
	private UserServiceClient userServiceClient;
	
	@Autowired
	public CategoryService(CategoryRepository repository) {
		this.repository = repository;
	}

	public Category save(Category category, int createdBy) {
		UserDTO userDTO;

		try {
			userDTO = userServiceClient.findById(createdBy);
		} catch (FeignException ex) {
			if (HttpStatus.NOT_FOUND.value() == ex.status()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nao existe usuario com id: " + createdBy);
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sistema indisponível no momento.");
			}
		}

		return repository.save(category);
	}

	public Iterable<Category> findAll() {
		return repository.findAll();
	}
	
	public Category findById(int id) {
		return repository.findById(id).get();
	}	

	public Category update(Category category) {
		return repository.save(category);
	}

	public void deleteById(int id) {
		repository.deleteById(id);
	}
}
