package com.br.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.entity.Income;
import com.br.repository.IncomeRepository;

@Service
public class IncomeService {
	
	@Autowired
	private  IncomeRepository repository;
	

	public List<Income> viewAllIncomes() {
		return repository.findAll();
	}


	public Income createIncome(Income income) {
		return repository.saveAndFlush(income);
	}

	public Income viewIncome(Integer id) {
		return repository.findById(id).get();
	}

	public Income updateIncome(Income income) {
		return repository.saveAndFlush(income);
	}

	public void deleteIncome(Integer id) {
		repository.deleteById(id);
	}
}
