package com.br;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.br.entity.SharedFinance;
import com.br.repository.SharedFinanceRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class SharedFinanceRepositoryTest {

	@Mock
	private SharedFinanceRepository repository;

	private SharedFinance sharedFinance;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findAll() {
		sharedFinance = new SharedFinance();
		List<SharedFinance> sharedFinanceList = new ArrayList<SharedFinance>();
		sharedFinanceList.add(sharedFinance);
		sharedFinanceList.add(sharedFinance);
		sharedFinanceList.add(sharedFinance);
		when(repository.findAll()).thenReturn((List<SharedFinance>) sharedFinanceList);
		List<SharedFinance> result = (List<SharedFinance>) repository.findAll();
		assertEquals(3, result.size());
	}

	@Test
	public void deleteById() {
		SharedFinance sharedFinance = new SharedFinance();
		repository.deleteById(sharedFinance.getId());
		verify(repository, times(1)).deleteById(sharedFinance.getId());
	}

	@Test
	public void save() {
		SharedFinance sharedFinance = new SharedFinance();
		when(repository.save(sharedFinance)).thenReturn(sharedFinance);
		SharedFinance result = repository.save(sharedFinance);
		assertEquals(1, result.getUserId());
		assertEquals(1, result.getSharedUserId());
		assertEquals(true, result.isAccepted());
		assertEquals("01-01-01 01:01:01", result.getCreatedAt());
	}

	@Test
	public void update() {
		SharedFinance sharedFinance = new SharedFinance();
		when(repository.save(sharedFinance)).thenReturn(sharedFinance);
		SharedFinance result = repository.save(sharedFinance);
		assertEquals(1, result.getUserId());
		assertEquals(1, result.getSharedUserId());
		assertEquals(true, result.isAccepted());
		assertEquals("01-01-01 01:01:01", result.getCreatedAt());	
	}

	@Test
	public void findById() {
		Optional<SharedFinance> sharedFinance = Optional.of(new SharedFinance());
		when(repository.findById(1)).thenReturn(sharedFinance);
		Optional<SharedFinance> result = repository.findById(1);
		assertEquals(1, result.get().getUserId());
		assertEquals(1, result.get().getSharedUserId());
		assertEquals(true, result.get().isAccepted());
		assertEquals("01-01-01 01:01:01", result.get().getCreatedAt());		
	}

}