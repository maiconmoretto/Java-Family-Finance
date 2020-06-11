package com.br;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.br.model.Expenses;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ExpensesControllerTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void findAll() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/expenses/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void save() throws Exception {
		Expenses expense = new Expenses("description", "01-01-01 01:01:01", 1, 3.33, 1);
		mvc.perform(MockMvcRequestBuilders.post("/api/v1/expenses/").content(asJsonString(expense))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

	}
	
	@Test
	public void update() throws Exception {
		Expenses expense = new Expenses();
		mvc.perform(MockMvcRequestBuilders.put("/api/v1/expenses/").content(asJsonString(expense))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

	}
	
	@Test
	public void delete() throws Exception {
		Expenses expense = new Expenses();
		mvc.perform(MockMvcRequestBuilders.delete("/api/v1/expenses/").content(asJsonString(expense))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

	}
	
	@Test
	public void findById() throws Exception {
		Expenses expense = new Expenses();
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/expenses/").content(asJsonString(expense))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

	}

	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
