
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

import com.br.entity.Income;

import com.br.service.IncomeService;

import java.util.List;
import java.util.Optional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("income")
public class IncomeController {

	@Autowired
	private IncomeService service;

	@ApiOperation(value = "It will return list of Income")
	@GetMapping
	public List<Income> viewAllIncomes() {
		return service.viewAllIncomes();
	}

	@ApiOperation(value = "It will add new Income")
	@PostMapping
	public Income createIncome(@RequestBody Income income) {
		return service.createIncome(income);
	}

	@ApiOperation(value = "It will get a Income by Id")
	@GetMapping("/{id}")
	public Income viewIncome(@PathVariable Integer id) {
		return service.viewIncome(id);
	}

	@ApiOperation(value = "It will update Income")
	@PutMapping("/{id}")
	public Income updateIncome(@PathVariable Integer id, @RequestBody Income income) {
		income.setId(id);
		return service.updateIncome(income);
	}

	@ApiOperation(value = "It will delete Income")
	@DeleteMapping("/{id}")
	public void deleteIncome(@PathVariable Integer id) {
		service.deleteIncome(id);
	}

}
