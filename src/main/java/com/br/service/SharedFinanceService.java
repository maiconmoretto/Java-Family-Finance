package com.br.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.br.model.SharedFinance;
import com.br.repository.SharedFinanceRepository;

@Service
public class SharedFinanceService {
	
	@Autowired
	SharedFinanceRepository repository;

	public Iterable<SharedFinance> findAll() {
		return repository.findAll();
	}
	
	public SharedFinance findById(int id) {
		return repository.findById(id).get();
	}	

	public ResponseEntity<String> save(SharedFinance expense) {
		repository.save(expense);
		return new ResponseEntity<>("SharedFinance successfully registered", HttpStatus.CREATED);
	}

	public ResponseEntity<String> update(SharedFinance expense) {
		Optional<SharedFinance> expenseExist = repository.findById(expense.getId());
		if (!expenseExist.isPresent()) {
			return new ResponseEntity<>("SharedFinance does not exist", HttpStatus.BAD_REQUEST);
		}
		repository.save(expense);
		return new ResponseEntity<>("SharedFinance successfully updated", HttpStatus.OK);
	}

	public ResponseEntity<String> deleteById(int id) {
		repository.deleteById(id);
		return new ResponseEntity<>("SharedFinance successfully deleted", HttpStatus.OK);
	}
}
