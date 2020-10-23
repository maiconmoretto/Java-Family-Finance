package com.br.service;

import java.util.List;

import com.br.entity.UserDTO;
import feign.FeignException;
import com.br.service.UserServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.entity.Category;
import com.br.service.UserServiceClient;
import com.br.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private  CategoryRepository repository;

	@Autowired
	private UserServiceClient userServiceClient;

	public List<Category> viewAllCategorys() {
		return repository.findAll();
	}


	public Category createCategory(Category category,int createdBy) {
		UserDTO userDTO;

		try {
			userDTO = userServiceClient.viewUser(createdBy);
		} catch (FeignException ex) {
			if (HttpStatus.NOT_FOUND.value() == ex.status()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nao existe usuario com id: " + createdBy);
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sistema indisponível no momento.");
			}
		}

		return repository.save(category);
	}



	public Category viewCategory(Integer id) {
		return repository.findById(id).get();
	}

	public Category updateCategory(Category category) {
		return repository.saveAndFlush(category);
	}

	public void deleteCategory(Integer id) {
		repository.deleteById(id);
	}
}
