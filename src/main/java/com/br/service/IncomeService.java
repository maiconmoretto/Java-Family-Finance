package com.br.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.br.model.Income;
import com.br.repository.IncomeRepository;

@Service
public class IncomeService {
	
	@Autowired
	IncomeRepository repository;

	public Iterable<Income> findAll() {
		return repository.findAll();
	}
	
	public Income findById(int id) {
		return repository.findById(id).get();
	}	

	public ResponseEntity<String> save(Income expense) {
		repository.save(expense);
		return new ResponseEntity<>("Income successfully registered", HttpStatus.CREATED);
	}

	public ResponseEntity<String> update(Income expense) {
		Optional<Income> expenseExist = repository.findById(expense.getId());
		if (!expenseExist.isPresent()) {
			return new ResponseEntity<>("Income does not exist", HttpStatus.BAD_REQUEST);
		}
		repository.save(expense);
		return new ResponseEntity<>("Income successfully updated", HttpStatus.OK);
	}

	public ResponseEntity<String> deleteById(int id) {
		repository.deleteById(id);
		return new ResponseEntity<>("Income successfully deleted", HttpStatus.OK);
	}
}
