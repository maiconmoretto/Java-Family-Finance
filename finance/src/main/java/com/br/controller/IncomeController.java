
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

import com.br.model.Income;
import com.br.model.IncomeDTO;
import com.br.service.IncomeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API Income")
@RequestMapping("/api/v1/income/")
public class IncomeController {
	private final IncomeService service;

	@Autowired
	public IncomeController(IncomeService service) {
		this.service = service;
	}

	@ApiOperation(value = "It will return list of Income")
	@GetMapping
	public @ResponseBody Iterable<Income> getAllIncomes() {
		return service.findAll();
	}

	@ApiOperation(value = "It will get a Income by Id")
	@GetMapping("/{id}")
	public Income findById(@PathVariable int id) {
		return service.findById(id);
	}
	
	@ApiOperation(value = "It will add new Income")
	@PostMapping
	public ResponseEntity<Income> save(@RequestBody IncomeDTO dto) {
	    Income income = service.save(dto.changeToObject());
	    return new ResponseEntity<>(income, HttpStatus.CREATED);
	}

	@ApiOperation(value = "It will update Income")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Income> update(@RequestBody Income income) {
		Income incomeSaved = service.update(income);
		return new ResponseEntity<>(incomeSaved, HttpStatus.CREATED);
	}

	@ApiOperation(value = "It will delete Income")
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		 service.deleteById(id);
		 return new ResponseEntity<>("Income deleted", HttpStatus.OK);
	}

}
