
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

import com.br.entity.Expense;

import com.br.service.ExpenseService;

import java.util.List;
import java.util.Optional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@Api(value = "API SharedFinance")
@RequestMapping("expense")
public class ExpenseController {

	@Autowired
	private ExpenseService service;

	@ApiOperation(value = "It will return list of Expense")
	@GetMapping
	public List<Expense> viewAllExpenses() {
		return service.viewAllExpenses();
	}

	@ApiOperation(value = "It will add new Expense")
	@PostMapping
	public Expense createExpense(@RequestBody Expense expense) {
		return service.createExpense(expense);
	}

	@ApiOperation(value = "It will get a Expense by Id")
	@GetMapping("/{id}")
	public Expense viewExpense(@PathVariable Integer id) {
		return service.viewExpense(id);
	}

	@ApiOperation(value = "It will update Expense")
	@PutMapping("/{id}")
	public Expense updateExpense(@PathVariable Integer id, @RequestBody Expense expense) {
		expense.setId(id);
		return service.updateExpense(expense);
	}

	@ApiOperation(value = "It will delete Expense")
	@DeleteMapping("/{id}")
	public void deleteExpense(@PathVariable Integer id) {
		service.deleteExpense(id);
	}

}
