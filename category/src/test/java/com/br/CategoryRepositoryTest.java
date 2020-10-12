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

import com.br.entity.Category;
import com.br.repository.CategoryRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class CategoryRepositoryTest {

	@Mock
	private CategoryRepository repository;

	private Category Category;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findAll() {
		Category = new Category();
		Category.setDescription("description");
		Category.setCreatedBy(1);
		List<Category> CategoryList = new ArrayList<Category>();
		CategoryList.add(Category);
		CategoryList.add(Category);
		CategoryList.add(Category);
		when(repository.findAll()).thenReturn((List<Category>) CategoryList);
		List<Category> result = (List<Category>) repository.findAll();
		assertEquals(3, result.size());
	}

	@Test
	public void deleteById() {
		Category = new Category();
		Category.setDescription("description");
		Category.setCreatedBy(1);
		Category.setId(1);
		repository.deleteById(Category.getId());
		verify(repository, times(1)).deleteById(Category.getId());
	}

	@Test
	public void save() {
		Category = new Category();
		Category.setDescription("description");
		Category.setCreatedBy(1);
		when(repository.save(Category)).thenReturn(Category);
		Category result = repository.save(Category);
		assertEquals("description", result.getDescription());
		assertEquals(1, result.getCreatedBy());
	}

	@Test
	public void update() {
		Category = new Category();
		Category.setDescription("description");
		Category.setCreatedBy(1);
		when(repository.save(Category)).thenReturn(Category);
		Category result = repository.save(Category);
		assertEquals("description", result.getDescription());
		assertEquals(1, result.getCreatedBy());
	}

	@Test
	public void findById() {
		Optional<Category> category = Optional.of(new Category());
		when(repository.findById(1)).thenReturn(category);
		Optional<Category> result = repository.findById(1);
		assertEquals("description", result.get().getDescription());
		assertEquals(1, result.get().getCreatedBy());
	}

}