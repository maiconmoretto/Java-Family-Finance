package com.br.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.br.model.Expenses;

public interface ExpensesRepository extends JpaRepository<Expenses, Integer> {

}