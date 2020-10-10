
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

@RestController
@RequestMapping("expense")
public class ExpenseController {

	@Autowired
	private ExpenseService service;

	@GetMapping
	public List<Expense> viewAllExpenses() {
		return service.viewAllExpenses();
	}

	@PostMapping
	public Expense createExpense(@RequestBody Expense expense) {
		return service.createExpense(expense);
	}

	@GetMapping("/{id}")
	public Expense viewExpense(@PathVariable Integer id) {
		return service.viewExpense(id);
	}

	@PutMapping("/{id}")
	public Expense updateExpense(@PathVariable Integer id, @RequestBody Expense expense) {
		expense.setId(id);
		return service.updateExpense(expense);
	}

	@DeleteMapping("/{id}")
	public void deleteExpense(@PathVariable Integer id) {
		service.deleteExpense(id);
	}

}
