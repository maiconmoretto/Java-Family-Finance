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

import com.br.model.Category;
import com.br.repository.CategoryRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class CategoryRepositoryTest {

	@Mock
	private CategoryRepository repository;

	private Category category;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findAll() {
		category = new Category();
		List<Category> categoryList = new ArrayList<Category>();
		categoryList.add(category);
		categoryList.add(category);
		categoryList.add(category);
		when(repository.findAll()).thenReturn((List<Category>) categoryList);
		List<Category> result = (List<Category>) repository.findAll();
		assertEquals(3, result.size());
	}

	@Test
	public void deleteById() {
		Category category = new Category("description", 1, 1);
		repository.deleteById(category.getId());
		verify(repository, times(1)).deleteById(category.getId());
	}

	@Test
	public void save() {
		Category category = new Category("description", 1, 1);
		when(repository.save(category)).thenReturn(category);
		Category result = repository.save(category);
		assertEquals("description", result.getDescription());
		assertEquals(1, result.getCreatedBy());
		assertEquals(1, result.getUpdatedBy());
	}

	@Test
	public void update() {
		Category category = new Category("description", 1, 1);
		when(repository.save(category)).thenReturn(category);
		Category result = repository.save(category);
		assertEquals("description", result.getDescription());
		assertEquals(1, result.getCreatedBy());
 		assertEquals(1, result.getUpdatedBy());
	}

	@Test
	public void findById() {
		Optional<Category> category = Optional.of(new Category("description", 1, 1));
		when(repository.findById(1)).thenReturn(category);
		Optional<Category> result = repository.findById(1);
		assertEquals("description", result.get().getDescription());
		assertEquals(1, result.get().getUpdatedBy());
		assertEquals(1, result.get().getCreatedBy());
	}


}
