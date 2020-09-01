package com.br.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.br.model.SharedFinance;

public interface SharedFinanceRepository extends JpaRepository<SharedFinance, Integer> {

}