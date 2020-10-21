
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

@RestController
@RequestMapping("sharedFinance")
public class SharedFinanceController {

	@Autowired
	private SharedFinanceService service;

	@GetMapping
	public List<SharedFinance> viewAllSharedFinances() {
		return service.viewAllSharedFinances();
	}

	@PostMapping
	public SharedFinance createSharedFinance(@RequestBody SharedFinance sharedFinance) {
		return service.createSharedFinance(sharedFinance);
	}

	@GetMapping("/{id}")
	public SharedFinance viewSharedFinance(@PathVariable Integer id) {
		return service.viewSharedFinance(id);
	}

	@PutMapping("/{id}")
	public SharedFinance updateSharedFinance(@PathVariable Integer id, @RequestBody SharedFinance sharedFinance) {
		sharedFinance.setId(id);
		return service.updateSharedFinance(sharedFinance);
	}

	@DeleteMapping("/{id}")
	public void deleteSharedFinance(@PathVariable Integer id) {
		service.deleteSharedFinance(id);
	}

}
