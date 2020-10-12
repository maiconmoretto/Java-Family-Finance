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

import com.br.entity.Income;
import com.br.repository.IncomeRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class IncomeRepositoryTest {

	@Mock
	private IncomeRepository repository;

	private Income income;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findAll() {
		income = new Income();
		List<Income> incomeList = new ArrayList<Income>();
		incomeList.add(income);
		incomeList.add(income);
		incomeList.add(income);
		when(repository.findAll()).thenReturn((List<Income>) incomeList);
		List<Income> result = (List<Income>) repository.findAll();
		assertEquals(3, result.size());
	}

	@Test
	public void deleteById() {
		Income income = new Income();
		repository.deleteById(income.getId());
		verify(repository, times(1)).deleteById(income.getId());
	}

	@Test
	public void save() {
		Income income = new Income();
		when(repository.save(income)).thenReturn(income);
		Income result = repository.save(income);
		assertEquals("Description", result.getDescription());
		assertEquals(3.33, result.getValue(), 0.00);
		assertEquals(1, result.getUpdatedBy());
	}

	@Test
	public void update() {
		Income income = new Income();
		when(repository.save(income)).thenReturn(income);
		Income result = repository.save(income);
		assertEquals("Description", result.getDescription());
		assertEquals(3.33, result.getValue(), 0.00);
		assertEquals(1, result.getUpdatedBy());
	}

	@Test
	public void findById() {
		Optional<Income> income = Optional.of(new Income());
		when(repository.findById(1)).thenReturn(income);
		Optional<Income> result = repository.findById(1);
		assertEquals("Description", result.get().getDescription());
		assertEquals(3.33, result.get().getValue(), 0.00);
		assertEquals(1, result.get().getUpdatedBy());
	}
}
