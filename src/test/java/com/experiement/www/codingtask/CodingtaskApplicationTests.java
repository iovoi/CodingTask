package com.experiement.www.codingtask;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.experiement.www.codingtask.controller.EnrichController;
import com.experiement.www.codingtask.controller.IndexController;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class CodingtaskApplicationTests {
	//@LocalServerPort
	//private int port;

	@Autowired
	private EnrichController enrichController;

	@Autowired
	private IndexController indexController;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
		assertThat(enrichController).isNotNull();
		assertThat(indexController).isNotNull();
		assertThat(restTemplate).isNotNull();
	}

	@Test
	void nomalCaseTest() {
		final String baseUrl = "http://localhost:8080/api/v1/enrich";
		URI uri;
		try {
			uri = new URI(baseUrl);
		} catch (Exception e) {
			uri = null;
			assertThat(e).isNull();
		}

		String postStr = "date,product_id,currency,price\n20160101,1,EUR,10.0\n20160101,2,EUR,20.1\n20160101,3,EUR,30.34\n20160101,11,EUR,35.34\n";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "text/csv");
		headers.set("Accept", "text/csv");

		HttpEntity<String> request = new HttpEntity<String>(postStr, headers);
		ResponseEntity<String> response = this.restTemplate.postForEntity(uri, request, String.class);
		String expectedStr = "date,product_id,currency,price\r\n20160101,Treasurey Bills Domestic,EUR,10\r\n20160101,Corporate Bonds Domestic,EUR,20.1\r\n20160101,REPO Domestic,EUR,30.34\r\n20160101,Missing Product Name,EUR,35.34\r\n";

		assertEquals(expectedStr, response.getBody().toString());
	}

	@Test
	void voidCaseTest() {
		final String baseUrl = "http://localhost:8080/api/v1/enrich";
		URI uri;
		try {
			uri = new URI(baseUrl);
		} catch (Exception e) {
			uri = null;
			assertThat(e).isNull();
		}

		String postStr = "";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "text/csv");
		headers.set("Accept", "text/csv");

		HttpEntity<String> request = new HttpEntity<String>(postStr, headers);
		ResponseEntity<String> response = this.restTemplate.postForEntity(uri, request, String.class);

		assertNull(response.getBody());
	}

}
