package com.br.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.br.entity.Income;

public interface IncomeRepository extends JpaRepository<Income, Integer> {

}