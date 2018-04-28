package com.n26.challenge.statistics.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.challenge.statistics.model.Transaction;
import com.n26.challenge.statistics.service.TransactionService;

/**
 * @author dsakho
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void testProcessAddNewTransactionRequest() throws Exception {
	final Transaction transactionRequest =
		new Transaction(Instant.now().toEpochMilli(), 52.40d);

	final String transactionString = new ObjectMapper().writeValueAsString(transactionRequest);

	when(transactionService.addTransaction(transactionRequest)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

	final MockHttpServletResponse response =
		mockMvc.perform(post("/transactions")
			.contentType(MediaType.APPLICATION_JSON)
			.content(transactionString))
		.andExpect(status().isCreated())
		.andDo(print())
		.andReturn()
		.getResponse();

	assertThat(response).isNotNull();
    }

    @Test
    public void testProcessAddOldTransactionRequest() throws Exception {
	final Transaction transactionRequest =
		new Transaction(Instant.now().minusSeconds(86400).toEpochMilli(), 52.40d);

	final String transactionString = new ObjectMapper().writeValueAsString(transactionRequest);

	when(transactionService.addTransaction(transactionRequest)).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());

	final MockHttpServletResponse response =
		mockMvc.perform(post("/transactions")
			.contentType(MediaType.APPLICATION_JSON)
			.content(transactionString))
		.andExpect(status().isNoContent())
		.andDo(print())
		.andReturn()
		.getResponse();

	assertThat(response).isNotNull();
    }

}
