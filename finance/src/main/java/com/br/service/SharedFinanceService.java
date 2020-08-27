package com.br.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.model.SharedFinance;
import com.br.repository.SharedFinanceRepository;

@Service
public class SharedFinanceService {
	
	private final SharedFinanceRepository repository;
	
	@Autowired
	public SharedFinanceService(SharedFinanceRepository repository) {
		this.repository = repository;
	}

	public SharedFinance save(SharedFinance sharedFinance) {
		return repository.save(sharedFinance);
	}

	public Iterable<SharedFinance> findAll() {
		return repository.findAll();
	}
	
	public SharedFinance findById(int id) {
		return repository.findById(id).get();
	}	

	public SharedFinance update(SharedFinance sharedFinance) {
		return repository.save(sharedFinance);
	}

	public void deleteById(int id) {
		repository.deleteById(id);
	}
}
