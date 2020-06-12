
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

import com.br.model.Expenses;
import com.br.service.ExpensesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API Expenses")
public class ExpensesController {
	@Autowired
	private ExpensesService service;

	@ApiOperation(value = "It will return list of Expenses")
	@GetMapping(path = "/api/v1/expenses/")
	public @ResponseBody Iterable<Expenses> getAllExpensess() {
		return service.findAll();
	}

	@ApiOperation(value = "It will get a Expense by Id")
	@GetMapping("/api/v1/expenses/{id}")
	public Expenses findById(@PathVariable int id) {
		return service.findById(id);
	}

	@ApiOperation(value = "It will add new Expense")
	@PostMapping(path = "/api/v1/expenses/")
	public @ResponseBody ResponseEntity save(@RequestBody Expenses expense) {
		return service.save(expense);
	}

	@ApiOperation(value = "It will update Expense")
	@PutMapping(value = "/api/v1/expenses/{id}")
	public ResponseEntity<String> update(@RequestBody Expenses expense) {
		return service.update(expense);
	}

	@ApiOperation(value = "It will delete Expense")
	@DeleteMapping(path = "/api/v1/expenses/{id}")
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		return service.deleteById(id);
	}

}