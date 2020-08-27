package com.br.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.br.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}