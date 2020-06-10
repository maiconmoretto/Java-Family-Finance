package com.br.repository;


import org.springframework.data.repository.CrudRepository;

import com.br.model.Expenses;

public interface ExpensesRepository extends CrudRepository<Expenses, Long> {

}