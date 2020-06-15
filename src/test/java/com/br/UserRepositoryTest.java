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

import com.br.model.User;
import com.br.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserRepositoryTest {

	@Mock
	private UserRepository repository;

	private User user;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findAll() {
		user = new User();
		List<User> userList = new ArrayList<User>();
		userList.add(user);
		userList.add(user);
		userList.add(user);
		when(repository.findAll()).thenReturn((List<User>) userList);
		List<User> result = (List<User>) repository.findAll();
		assertEquals(3, result.size());
	}

	@Test
	public void deleteById() {
		User user = new User("Joao", "01-01-01 01:01:01", "joao@joao.com");
		repository.deleteById(user.getId());
		verify(repository, times(1)).deleteById(user.getId());
	}

	@Test
	public void save() {
		User user = new User("Joao", "01-01-01 01:01:01", "joao@joao.com");
		when(repository.save(user)).thenReturn(user);
		User result = repository.save(user);
		assertEquals("Joao", result.getName());
		assertEquals("joao@joao.com", result.getEmail());
		assertEquals("01-01-01 01:01:01", result.getCreatedAt());
	}

	@Test
	public void update() {
		User user = new User("Joao", "01-01-01 01:01:01", "joao@joao.com");
		when(repository.save(user)).thenReturn(user);
		User result = repository.save(user);
		assertEquals("Joao", result.getName());
		assertEquals("joao@joao.com", result.getEmail());
		assertEquals("01-01-01 01:01:01", result.getCreatedAt());
	}

	@Test
	public void findById() {
		Optional<User> user = Optional.of(new User("Joao", "01-01-01 01:01:01", "joao@joao.com"));
		when(repository.findById(1)).thenReturn(user);
		Optional<User> result = repository.findById(1);
		assertEquals("Joao", result.get().getName());
		assertEquals("joao@joao.com", result.get().getEmail());
		assertEquals("01-01-01 01:01:01", result.get().getCreatedAt());		
	}

}