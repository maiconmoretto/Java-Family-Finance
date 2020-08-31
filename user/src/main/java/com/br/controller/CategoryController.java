
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

import com.br.model.Category;
import com.br.model.CategoryDTO;
import com.br.service.CategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API Category")
@RequestMapping("/api/v1/category/")
public class CategoryController {
	private final CategoryService service;

	@Autowired
	public CategoryController(CategoryService service) {
		this.service = service;
	}

	@ApiOperation(value = "It will return list of Category")
	@GetMapping
	public @ResponseBody Iterable<Category> getAllCategorys() {
		return service.findAll();
	}

	@ApiOperation(value = "It will get a Category by Id")
	@GetMapping("/{id}")
	public Category findById(@PathVariable int id) {
		return service.findById(id);
	}
	
	@ApiOperation(value = "It will add new Category")
	@PostMapping
	public ResponseEntity<Category> save(@RequestBody CategoryDTO dto) {
	    Category category = service.save(dto.changeToObject());
	    return new ResponseEntity<>(category, HttpStatus.CREATED);
	}

	@ApiOperation(value = "It will update Category")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Category> update(@RequestBody Category category) {
		Category categorySaved = service.update(category);
		return new ResponseEntity<>(categorySaved, HttpStatus.CREATED);
	}

	@ApiOperation(value = "It will delete Category")
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		 service.deleteById(id);
		 return new ResponseEntity<>("Category deleted", HttpStatus.OK);
	}

}
