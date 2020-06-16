
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

import com.br.model.Income;
import com.br.service.IncomeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API Income")
public class IncomeController {
	@Autowired
	private IncomeService service;

	@ApiOperation(value = "It will return list of Income")
	@GetMapping(path = "/api/v1/income/")
	public @ResponseBody Iterable<Income> getAllIncomes() {
		return service.findAll();
	}

	@ApiOperation(value = "It will get a Income by Id")
	@GetMapping("/api/v1/income/{id}")
	public Income findById(@PathVariable int id) {
		return service.findById(id);
	}

	@ApiOperation(value = "It will add new Income")
	@PostMapping(path = "/api/v1/income/")
	public @ResponseBody ResponseEntity save(@RequestBody Income expense) {
		return service.save(expense);
	}

	@ApiOperation(value = "It will update Income")
	@PutMapping(value = "/api/v1/income/{id}")
	public ResponseEntity<String> update(@RequestBody Income expense) {
		return service.update(expense);
	}

	@ApiOperation(value = "It will delete Income")
	@DeleteMapping(path = "/api/v1/income/{id}")
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		return service.deleteById(id);
	}

}