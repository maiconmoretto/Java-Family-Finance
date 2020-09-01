package com.br.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.model.Category;
import com.br.repository.CategoryRepository;

@Service
public class CategoryService {
	
	private final CategoryRepository repository;
	
	@Autowired
	public CategoryService(CategoryRepository repository) {
		this.repository = repository;
	}

	public Category save(Category category) {
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
