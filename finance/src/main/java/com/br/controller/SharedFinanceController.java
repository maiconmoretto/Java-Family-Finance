
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

import com.br.model.SharedFinance;
import com.br.model.SharedFinanceDTO;
import com.br.service.SharedFinanceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API SharedFinance")
@RequestMapping("/api/v1/sharedFinance/")
public class SharedFinanceController {
	private final SharedFinanceService service;

	@Autowired
	public SharedFinanceController(SharedFinanceService service) {
		this.service = service;
	}

	@ApiOperation(value = "It will return list of SharedFinance")
	@GetMapping
	public @ResponseBody Iterable<SharedFinance> getAllSharedFinances() {
		return service.findAll();
	}

	@ApiOperation(value = "It will get a SharedFinance by Id")
	@GetMapping("/{id}")
	public SharedFinance findById(@PathVariable int id) {
		return service.findById(id);
	}
	
	@ApiOperation(value = "It will add new SharedFinance")
	@PostMapping
	public ResponseEntity<SharedFinance> save(@RequestBody SharedFinanceDTO dto) {
	    SharedFinance sharedFinance = service.save(dto.changeToObject());
	    return new ResponseEntity<>(sharedFinance, HttpStatus.CREATED);
	}

	@ApiOperation(value = "It will update SharedFinance")
	@PutMapping(value = "/{id}")
	public ResponseEntity<SharedFinance> update(@RequestBody SharedFinance sharedFinance) {
		SharedFinance sharedFinanceSaved = service.update(sharedFinance);
		return new ResponseEntity<>(sharedFinanceSaved, HttpStatus.CREATED);
	}

	@ApiOperation(value = "It will delete SharedFinance")
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		 service.deleteById(id);
		 return new ResponseEntity<>("SharedFinance deleted", HttpStatus.OK);
	}

}
