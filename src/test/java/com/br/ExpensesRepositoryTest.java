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

import com.br.model.Expenses;
import com.br.repository.ExpensesRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class ExpensesRepositoryTest {

	@Mock
	private ExpensesRepository repository;

	private Expenses expense;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findAll() {
		expense = new Expenses();
		expense.setCreatedAt("01-01-01 01:01:01");
		expense.setDescription("description");
		expense.setValue(3.33);
		expense.setCreatedBy(1);
		List<Expenses> expenseList = new ArrayList<Expenses>();
		expenseList.add(expense);
		expenseList.add(expense);
		expenseList.add(expense);
		when(repository.findAll()).thenReturn((Iterable<Expenses>) expenseList);
		List<Expenses> result = (List<Expenses>) repository.findAll();
		assertEquals(3, result.size());
	}

	@Test
	public void deleteById() {
		expense = new Expenses();
		expense.setCreatedAt("01-01-01 01:01:01");
		expense.setDescription("description");
		expense.setValue(3.33);
		expense.setCreatedBy(1);
		expense.setId((long )1);
		repository.deleteById((long) expense.getId());
		verify(repository, times(1)).deleteById((long) expense.getId());
	}

	@Test
	public void save() {
		expense = new Expenses();
		expense.setCreatedAt("01-01-01 01:01:01");
		expense.setDescription("description");
		expense.setValue(3.33);
		expense.setCreatedBy(1);
		when(repository.save(expense)).thenReturn(expense);
		Expenses result = repository.save(expense);
		assertEquals("description", result.getDescription());
		assertEquals("01-01-01 01:01:01", result.getCreatedAt());
		assertEquals(3.33, result.getValue(), 0.00);
		assertEquals(1, result.getCreatedBy());
	}

	@Test
	public void update() {
		expense = new Expenses();
		expense.setCreatedAt("01-01-01 01:01:01");
		expense.setDescription("description");
		expense.setValue(3.33);
		expense.setCreatedBy(1);
		when(repository.save(expense)).thenReturn(expense);
		Expenses result = repository.save(expense);
		assertEquals("description", result.getDescription());
		assertEquals("01-01-01 01:01:01", result.getCreatedAt());
		assertEquals(3.33, result.getValue(), 0.0);
		assertEquals(1, result.getCreatedBy());
	}

	@Test
	public void findById() {
		Optional<Expenses> agenda = Optional.of(new Expenses("description", "01-01-01 01:01:01", 1, 3.33, 1, 1));
		when(repository.findById((long) 1)).thenReturn(agenda);
		Optional<Expenses> result = repository.findById((long) 1);
		assertEquals("description", result.get().getDescription());
		assertEquals("01-01-01 01:01:01", result.get().getCreatedAt());
		assertEquals(3.33, result.get().getValue(), 0.00);
		assertEquals(1, result.get().getCreatedBy());
		;
	}

}