
package com.br.controller;

import java.util.Optional;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.model.Expenses;
import com.br.repository.ExpensesRepository;
import com.br.service.ExpensesService;

@RestController
public class ExpensesController {
  @Autowired 
  private ExpensesService service;


  @GetMapping(path="/api/v1/expenses/")
  public  @ResponseBody Iterable<Expenses> getAllExpensess() {
      return service.findAll();
  } 

}