package com.ewolff.microservice.invoicing;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.ewolff.microservice.invoicing.InvoiceRepository;
import com.ewolff.microservice.invoicing.poller.InvoicePoller;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = InvoiceTestApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class InvoiceWebIntegrationTest {

	@LocalServerPort
	private int serverPort;

	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private InvoiceRepository orderRepository;

	@Autowired
	private InvoicePoller orderPoller;

	@Test
	public void isHTMLReturned() {
		String body = getForMediaType(String.class, MediaType.TEXT_HTML, shippingURL());

		assertThat(body, containsString("<div"));
	}

	@Test
	public void orderArePolled() {
		long countBeforePoll = orderRepository.count();
		orderPoller.poll();
		assertThat(orderRepository.count(), is(greaterThan(countBeforePoll)));
	}

	private String shippingURL() {
		return "http://localhost:" + serverPort;
	}

	private <T> T getForMediaType(Class<T> value, MediaType mediaType, String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(mediaType));

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		ResponseEntity<T> resultEntity = restTemplate.exchange(url, HttpMethod.GET, entity, value);

		return resultEntity.getBody();
	}

}
