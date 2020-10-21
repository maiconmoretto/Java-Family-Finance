package com.br.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.entity.SharedFinance;
import com.br.repository.SharedFinanceRepository;

@Service
public class SharedFinanceService {
	
	@Autowired
	private  SharedFinanceRepository repository;
	

	public List<SharedFinance> viewAllSharedFinances() {
		return repository.findAll();
	}


	public SharedFinance createSharedFinance(SharedFinance sharedFinance) {
		return repository.saveAndFlush(sharedFinance);
	}

	public SharedFinance viewSharedFinance(Integer id) {
		return repository.findById(id).get();
	}

	public SharedFinance updateSharedFinance(SharedFinance sharedFinance) {
		return repository.saveAndFlush(sharedFinance);
	}

	public void deleteSharedFinance(Integer id) {
		repository.deleteById(id);
	}
}
