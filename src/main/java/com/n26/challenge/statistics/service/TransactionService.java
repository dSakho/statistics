package com.n26.challenge.statistics.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.function.BiPredicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.n26.challenge.statistics.model.Transaction;
import com.n26.challenge.statistics.repository.TransactionDao;

/**
 * @author dsakho
 *
 */
@Service
public class TransactionService {

    private TransactionDao transactionDao;

    private final BiPredicate<LocalDateTime, LocalDateTime> durationPredicate = (a, b) -> {
	return Duration.between(a, b).getSeconds() > Duration.ofSeconds(60).getSeconds();
    };

    public ResponseEntity<?> addTransaction(final Transaction transaction) {

	final LocalDateTime now = LocalDateTime.now();

	transactionDao.addTransaction(transaction);

	final LocalDateTime transactionTime =
		LocalDateTime.ofInstant(Instant.ofEpochMilli(transaction.getTimestamp()), ZoneOffset.UTC);

	// Returns 204 if transaction is older than 60 seconds, 201 otherwise
	if (durationPredicate.test(transactionTime, now)) {
	    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	} else {
	    return ResponseEntity.status(HttpStatus.CREATED).build();
	}
    }

    @Autowired
    public void setTransactionDao(final TransactionDao transactionDao) {
	this.transactionDao = transactionDao;
    }
}
