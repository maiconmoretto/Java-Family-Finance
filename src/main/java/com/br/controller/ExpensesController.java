
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

import com.br.model.Expenses;
import com.br.model.ExpensesDTO;
import com.br.service.ExpensesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API Expenses")
@RequestMapping("/api/v1/expenses/")
public class ExpensesController {
	private final ExpensesService service;

	@Autowired
	public ExpensesController(ExpensesService service) {
		this.service = service;
	}

	@ApiOperation(value = "It will return list of Expenses")
	@GetMapping
	public @ResponseBody Iterable<Expenses> getAllExpensess() {
		return service.findAll();
	}

	@ApiOperation(value = "It will get a Expenses by Id")
	@GetMapping("/{id}")
	public Expenses findById(@PathVariable int id) {
		return service.findById(id);
	}
	
	@ApiOperation(value = "It will add new Expenses")
	@PostMapping
	public ResponseEntity<Expenses> save(@RequestBody ExpensesDTO dto) {
	    Expenses expenses = service.save(dto.changeToObject());
	    return new ResponseEntity<>(expenses, HttpStatus.CREATED);
	}

	@ApiOperation(value = "It will update Expenses")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Expenses> update(@RequestBody Expenses expenses) {
		Expenses expensesSaved = service.update(expenses);
		return new ResponseEntity<>(expensesSaved, HttpStatus.CREATED);
	}

	@ApiOperation(value = "It will delete Expenses")
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		 service.deleteById(id);
		 return new ResponseEntity<>("Expenses deleted", HttpStatus.OK);
	}

}
