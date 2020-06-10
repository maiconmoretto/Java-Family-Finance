package com.repository;


import org.springframework.data.repository.CrudRepository;

import com.model.Expenses;

public interface ExpensesRepository extends CrudRepository<Expenses, Long> {

}