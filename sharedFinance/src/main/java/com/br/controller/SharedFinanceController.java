
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

import com.br.entity.SharedFinance;

import com.br.service.SharedFinanceService;

import java.util.List;
import java.util.Optional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API SharedFinance")
@RequestMapping("sharedFinance")
public class SharedFinanceController {

	@Autowired
	private SharedFinanceService service;

	@ApiOperation(value = "It will return list of SharedFinance")
	@GetMapping
	public List<SharedFinance> viewAllSharedFinances() {
		return service.viewAllSharedFinances();
	}

	@ApiOperation(value = "It will add new SharedFinance")
	@PostMapping
	public SharedFinance createSharedFinance(@RequestBody SharedFinance sharedFinance) {
		return service.createSharedFinance(sharedFinance);
	}

	@ApiOperation(value = "It will get a SharedFinance by Id")
	@GetMapping("/{id}")
	public SharedFinance viewSharedFinance(@PathVariable Integer id) {
		return service.viewSharedFinance(id);
	}

	@ApiOperation(value = "It will update SharedFinance")
	@PutMapping("/{id}")
	public SharedFinance updateSharedFinance(@PathVariable Integer id, @RequestBody SharedFinance sharedFinance) {
		sharedFinance.setId(id);
		return service.updateSharedFinance(sharedFinance);
	}

	@ApiOperation(value = "It will delete  SharedFinance")
	@DeleteMapping("/{id}")
	public void deleteSharedFinance(@PathVariable Integer id) {
		service.deleteSharedFinance(id);
	}

}
