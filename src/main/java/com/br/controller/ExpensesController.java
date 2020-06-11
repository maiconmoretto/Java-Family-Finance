
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

@RestController
public class ExpensesController {
	@Autowired
	private ExpensesService service;

	@GetMapping(path = "/api/v1/expenses/")
	public @ResponseBody Iterable<Expenses> getAllExpensess() {
		return service.findAll();
	}

	@GetMapping("/api/v1/expenses/{id}")
	public Expenses findById(@PathVariable int id) {
		return service.findById(id);
	}

	@PostMapping(path = "/api/v1/expenses/")
	public @ResponseBody ResponseEntity save(@RequestBody Expenses expense) {
		return service.save(expense);
	}

	@PutMapping(value = "/api/v1/expenses/{id}")
	public ResponseEntity<String> update(@RequestBody Expenses expense) {
		return service.update(expense);
	}

	@DeleteMapping(path = "/api/v1/expenses/{id}")
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		return service.deleteById(id);
	}

}