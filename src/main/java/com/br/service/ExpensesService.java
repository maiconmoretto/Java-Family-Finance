package com.br.service;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.model.Agenda;
import com.br.model.Expenses;
import com.br.repository.ExpensesRepository;

@Service
public class ExpensesService {
	
	@Autowired
	ExpensesRepository repository;
	
	@Before
	public void setup() {
		Expenses = new Expenses("agenda 1", "01-01-01", 60, 0, 0);
	}

	public Iterable<Expenses> findAll() {
		return repository.findAll();
	}
}
