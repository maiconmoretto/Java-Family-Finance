package com;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ExpensesTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void getAllTest() {
		String body = this.restTemplate.getForObject("/expenses/", String.class);
		assertThat(body).isEqualTo("[{\"id\":1,\"idUser\":1,\"dateCreated\":null,\"description\":\"teste 1\",\"dateUpdated\":null,\"idCategory\":1,\"createdBy\":1},{\"id\":2,\"idUser\":1,\"dateCreated\":null,\"description\":\"teste 1\",\"dateUpdated\":null,\"idCategory\":1,\"createdBy\":1}]");
	}

}