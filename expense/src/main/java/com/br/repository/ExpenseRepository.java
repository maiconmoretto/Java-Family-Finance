package com.br.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.br.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

}