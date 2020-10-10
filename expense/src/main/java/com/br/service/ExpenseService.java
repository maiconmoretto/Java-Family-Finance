package com.br.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.entity.Expense;
import com.br.repository.ExpenseRepository;

@Service
public class ExpenseService {
	
	@Autowired
	private  ExpenseRepository repository;
	

	public List<Expense> viewAllExpenses() {
		return repository.findAll();
	}


	public Expense createExpense(Expense category) {
		return repository.saveAndFlush(category);
	}

	public Expense viewExpense(Integer id) {
		return repository.findById(id).get();
	}

	public Expense updateExpense(Expense category) {
		return repository.saveAndFlush(category);
	}

	public void deleteExpense(Integer id) {
		repository.deleteById(id);
	}
}
