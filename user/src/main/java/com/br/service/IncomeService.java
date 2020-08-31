package com.br.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.model.Income;
import com.br.repository.IncomeRepository;

@Service
public class IncomeService {
	
	private final IncomeRepository repository;
	
	@Autowired
	public IncomeService(IncomeRepository repository) {
		this.repository = repository;
	}

	public Income save(Income income) {
		return repository.save(income);
	}

	public Iterable<Income> findAll() {
		return repository.findAll();
	}
	
	public Income findById(int id) {
		return repository.findById(id).get();
	}	

	public Income update(Income income) {
		return repository.save(income);
	}

	public void deleteById(int id) {
		repository.deleteById(id);
	}
}
