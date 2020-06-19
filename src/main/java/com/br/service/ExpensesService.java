package com.br.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.model.Expenses;
import com.br.repository.ExpensesRepository;

@Service
public class ExpensesService {
	
	private final ExpensesRepository repository;
	
	@Autowired
	public ExpensesService(ExpensesRepository repository) {
		this.repository = repository;
	}

	public Expenses save(Expenses expenses) {
		return repository.save(expenses);
	}

	public Iterable<Expenses> findAll() {
		return repository.findAll();
	}
	
	public Expenses findById(int id) {
		return repository.findById(id).get();
	}	

	public Expenses update(Expenses expenses) {
		return repository.save(expenses);
	}

	public void deleteById(int id) {
		repository.deleteById(id);
	}
}
