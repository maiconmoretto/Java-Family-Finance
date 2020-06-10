package com.br.service;

import org.springframework.beans.factory.annotation.Autowired;
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
}
