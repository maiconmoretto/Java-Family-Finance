
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.br.entity.Category;
import com.br.service.CategoryService;

import java.util.List;
import java.util.Optional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API Category")
@RequestMapping("category")
public class CategoryController {

	@Autowired
	private CategoryService service;

	@ApiOperation(value = "It will return list of Category")
	@GetMapping
	public List<Category> viewAllCategorys() {
		return service.viewAllCategorys();
	}

	@ApiOperation(value = "It will add new Category")
	@PostMapping
	public ResponseEntity<Category> save(@RequestBody Category category, int createdBy) {
		return new ResponseEntity<>(service.createCategory(category, createdBy), HttpStatus.CREATED);
	}

	@ApiOperation(value = "It will get a Category by Id")
	@GetMapping("/{id}")
	public Category viewCategory(@PathVariable Integer id) {
		return service.viewCategory(id);
	}

	@ApiOperation(value = "It will update Category")
	@PutMapping("/{id}")
	public Category updateCategory(@PathVariable Integer id, @RequestBody Category category) {
		category.setId(id);
		return service.updateCategory(category);
	}

	@ApiOperation(value = "It will delete  Category")
	@DeleteMapping("/{id}")
	public void deleteCategory(@PathVariable Integer id) {
		service.deleteCategory(id);
	}

}
