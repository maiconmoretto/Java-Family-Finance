package com.br.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.br.model.Expenses;
import com.br.repository.ExpensesRepository;

@Service
public class ExpensesService {
	
	@Autowired
	ExpensesRepository repository;

	public Iterable<Expenses> findAll() {
		return repository.findAll();
	}
	
	public Expenses findById(int id) {
		return repository.findById(id).get();
	}	

	public ResponseEntity<String> save(Expenses expense) {
		repository.save(expense);
		return new ResponseEntity<>("Expenses successfully registered", HttpStatus.CREATED);
	}

	public ResponseEntity<String> update(Expenses expense) {
		Optional<Expenses> expenseExist = repository.findById(expense.getId());
		if (!expenseExist.isPresent()) {
			return new ResponseEntity<>("Expenses does not exist", HttpStatus.BAD_REQUEST);
		}
		repository.save(expense);
		return new ResponseEntity<>("Expenses successfully updated", HttpStatus.OK);
	}

	public ResponseEntity<String> deleteById(int id) {
		repository.deleteById(id);
		return new ResponseEntity<>("Expenses successfully deleted", HttpStatus.OK);
	}
}
