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

import com.br.model.Income;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class IncomeControllerTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void findAll() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/income/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void save() throws Exception {
		Income income = new Income("despesa", "01-01-01 01:01:01", 3, 3.33);
		mvc.perform(MockMvcRequestBuilders.post("/api/v1/income/").content(asJsonString(income))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

	}
	
	@Test
	public void update() throws Exception {
		Income income = new Income();
		mvc.perform(MockMvcRequestBuilders.put("/api/v1/income/").content(asJsonString(income))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

	}
	
	@Test
	public void delete() throws Exception {
		Income income = new Income();
		mvc.perform(MockMvcRequestBuilders.delete("/api/v1/income/").content(asJsonString(income))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

	}
	
	@Test
	public void findById() throws Exception {
		Income income = new Income();
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/income/").content(asJsonString(income))
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
