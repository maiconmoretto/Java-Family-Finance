
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

import com.br.entity.Category;

import com.br.service.CategoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("category")
public class CategoryController {

	@Autowired
	private CategoryService service;

	@GetMapping
	public List<Category> viewAllCategorys() {
		return service.viewAllCategorys();
	}

	@PostMapping
	public Category createCategory(@RequestBody Category category) {
		return service.createCategory(category);
	}

	@GetMapping("/{id}")
	public Category viewCategory(@PathVariable Integer id) {
		return service.viewCategory(id);
	}

	@PutMapping("/{id}")
	public Category updateCategory(@PathVariable Integer id, @RequestBody Category category) {
		category.setId(id);
		return service.updateCategory(category);
	}

	@DeleteMapping("/{id}")
	public void deleteCategory(@PathVariable Integer id) {
		service.deleteCategory(id);
	}

}
