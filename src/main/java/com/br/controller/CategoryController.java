
package com.br.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.model.Category;
import com.br.service.CategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API Category")
public class CategoryController {
	@Autowired
	private CategoryService service;

	@ApiOperation(value = "It will return list of Category")
	@GetMapping(path = "/api/v1/category/")
	public @ResponseBody Iterable<Category> getAllCategorys() {
		return service.findAll();
	}

	@ApiOperation(value = "It will get a Category by Id")
	@GetMapping("/api/v1/category/{id}")
	public Category findById(@PathVariable int id) {
		return service.findById(id);
	}

	@ApiOperation(value = "It will add new Category")
	@PostMapping(path = "/api/v1/category/")
	public @ResponseBody ResponseEntity save(@RequestBody Category expense) {
		return service.save(expense);
	}

	@ApiOperation(value = "It will update Category")
	@PutMapping(value = "/api/v1/category/{id}")
	public ResponseEntity<String> update(@RequestBody Category expense) {
		return service.update(expense);
	}

	@ApiOperation(value = "It will delete Category")
	@DeleteMapping(path = "/api/v1/category/{id}")
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		return service.deleteById(id);
	}

}