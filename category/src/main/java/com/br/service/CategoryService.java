package com.br.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.entity.Category;
import com.br.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private  CategoryRepository repository;
	

	public List<Category> viewAllCategorys() {
		return repository.findAll();
	}


	public Category createCategory(Category category) {
		return repository.saveAndFlush(category);
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
