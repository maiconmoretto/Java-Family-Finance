package com.br;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Id;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.br.entity.Expense;
import com.br.repository.ExpenseRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class ExpenseRepositoryTest {

	@Mock
	private ExpenseRepository repository;

	private Expense expense;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findAll() {
		expense = new Expense();
		expense.setDescription("description");
		expense.setValue(3.33);
		expense.setCreatedBy(1);
		List<Expense> expenseList = new ArrayList<Expense>();
		expenseList.add(expense);
		expenseList.add(expense);
		expenseList.add(expense);
		when(repository.findAll()).thenReturn((List<Expense>) expenseList);
		List<Expense> result = (List<Expense>) repository.findAll();
		assertEquals(3, result.size());
	}

	@Test
	public void deleteById() {
		expense = new Expense();
		expense.setDescription("description");
		expense.setValue(3.33);
		expense.setCreatedBy(1);
		expense.setId(1);
		repository.deleteById(expense.getId());
		verify(repository, times(1)).deleteById(expense.getId());
	}

	@Test
	public void save() {
		expense = new Expense();
		expense.setDescription("description");
		expense.setValue(3.33);
		expense.setCreatedBy(1);
		when(repository.save(expense)).thenReturn(expense);
		Expense result = repository.save(expense);
		assertEquals("description", result.getDescription());
		assertEquals(3.33, result.getValue(), 0.00);
		assertEquals(1, result.getCreatedBy());
	}

	@Test
	public void update() {
		expense = new Expense();
		expense.setDescription("description");
		expense.setValue(3.33);
		expense.setCreatedBy(1);
		when(repository.save(expense)).thenReturn(expense);
		Expense result = repository.save(expense);
		assertEquals("description", result.getDescription());
		assertEquals(3.33, result.getValue(), 0.0);
		assertEquals(1, result.getCreatedBy());
	}

	@Test
	public void findById() {
		Optional<Expense> agenda = Optional.of(new Expense());
		when(repository.findById(1)).thenReturn(agenda);
		Optional<Expense> result = repository.findById(1);
		assertEquals("description", result.get().getDescription());
		assertEquals(3.33, result.get().getValue(), 0.00);
		assertEquals(1, result.get().getCreatedBy());
		;
	}

}