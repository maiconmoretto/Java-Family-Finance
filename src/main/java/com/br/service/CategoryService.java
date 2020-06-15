package com.br.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.br.model.Category;
import com.br.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	CategoryRepository repository;
 
	public Iterable<Category> findAll() {
		return repository.findAll();
	}
	
	public Category findById(int id) {
		return repository.findById(id).get();
	}	

	public ResponseEntity<String> save(Category expense) {
		repository.save(expense);
		return new ResponseEntity<>("Category successfully registered", HttpStatus.CREATED);
	}

	public ResponseEntity<String> update(Category expense) {
		Optional<Category> expenseExist = repository.findById(expense.getId());
		if (!expenseExist.isPresent()) {
			return new ResponseEntity<>("Category does not exist", HttpStatus.BAD_REQUEST);
		}
		repository.save(expense);
		return new ResponseEntity<>("Category successfully updated", HttpStatus.OK);
	}

	public ResponseEntity<String> deleteById(int id) {
		repository.deleteById(id);
		return new ResponseEntity<>("Category successfully deleted", HttpStatus.OK);
	}
}
