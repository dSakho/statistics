package com.n26.challenge.statistics.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

import java.time.Instant;
import java.time.ZoneId;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.challenge.statistics.model.Transaction;
import com.n26.challenge.statistics.repository.TransactionDao;

/**
 * @author dsakho
 *
 */
@RunWith(SpringRunner.class)
public class TransactionServiceTest {

    private TransactionService transactionService;

    @MockBean
    private TransactionDao transactionDao;

    @Before
    public void setUp() {
	transactionService = new TransactionService();
	transactionService.setTransactionDao(transactionDao);
    }

    @Test
    public void testAddRecentTransaction() throws Exception {
	final Transaction recentTransactionSpy =
		spy(new Transaction(Instant.now().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(), 50.50d));

	doNothing().when(transactionDao).addTransaction(recentTransactionSpy);

	final ResponseEntity<?> response = transactionService.addTransaction(recentTransactionSpy);

	assertThat(response).isNotNull().isEqualTo(ResponseEntity.status(HttpStatus.CREATED).build());
    }

    @Test
    public void testAddOldTransaction() throws Exception {
	final Transaction oldTransactionSpy =
		spy(new Transaction(Instant.now().minusSeconds(86400).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(), 50.50d));

	doNothing().when(transactionDao).addTransaction(oldTransactionSpy);

	final ResponseEntity<?> response = transactionService.addTransaction(oldTransactionSpy);

	assertThat(response).isNotNull().isEqualTo(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }


}
