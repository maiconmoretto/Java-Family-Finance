package com.br.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.br.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}