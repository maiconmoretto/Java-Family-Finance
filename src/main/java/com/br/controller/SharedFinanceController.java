
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

import com.br.model.SharedFinance;
import com.br.service.SharedFinanceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API SharedFinance")
public class SharedFinanceController {
	@Autowired
	private SharedFinanceService service;

	@ApiOperation(value = "It will return list of SharedFinance")
	@GetMapping(path = "/api/v1/sharedFinance/")
	public @ResponseBody Iterable<SharedFinance> getAllSharedFinances() {
		return service.findAll();
	}

	@ApiOperation(value = "It will get a SharedFinance by Id")
	@GetMapping("/api/v1/sharedFinance/{id}")
	public SharedFinance findById(@PathVariable int id) {
		return service.findById(id);
	}

	@ApiOperation(value = "It will add new SharedFinance")
	@PostMapping(path = "/api/v1/sharedFinance/")
	public @ResponseBody ResponseEntity save(@RequestBody SharedFinance expense) {
		return service.save(expense);
	}

	@ApiOperation(value = "It will update SharedFinance")
	@PutMapping(value = "/api/v1/sharedFinance/{id}")
	public ResponseEntity<String> update(@RequestBody SharedFinance expense) {
		return service.update(expense);
	}

	@ApiOperation(value = "It will delete SharedFinance")
	@DeleteMapping(path = "/api/v1/sharedFinance/{id}")
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		return service.deleteById(id);
	}

}