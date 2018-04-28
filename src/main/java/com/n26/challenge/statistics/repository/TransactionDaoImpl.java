package com.n26.challenge.statistics.repository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;

import com.n26.challenge.statistics.model.Transaction;

/**
 * @author dsakho
 *
 */
public class TransactionDaoImpl implements TransactionDao {

    private final ConcurrentMap<Long, Double> transactionRepository;

    public TransactionDaoImpl() {
	transactionRepository = new ConcurrentHashMap<>();
    }

    @Override
    public Collection<Transaction> findAll() {
	return transactionRepository.entrySet()
		.stream()
		.map(entry -> {
		    return new Transaction(entry.getKey(), entry.getValue());
		})
		.collect(Collectors.toSet());
    }

    @Override
    public Collection<Transaction> findAllByDuration(final Duration timeDuration) {
	return transactionRepository.entrySet()
		.stream()
		.filter(entry -> {
		    final LocalDateTime transactionTime =
			    LocalDateTime.ofInstant(Instant.ofEpochMilli(entry.getKey()), ZoneOffset.UTC);

		    final LocalDateTime now = LocalDateTime.now();

		    return Duration.between(transactionTime, now).getSeconds() <= timeDuration.getSeconds();
		})
		.map(entry -> {
		    return new Transaction(entry.getKey(), entry.getValue());
		})
		.collect(Collectors.toSet());
    }

    @Override
    public Double getAmountFromTimeStamp(final Long timestamp) {
	return transactionRepository.get(timestamp);
    }

    @Async("transactionExecutor")
    @Override
    public void addTransaction(final Transaction newTransaction) {
	transactionRepository.put(newTransaction.getTimestamp(), newTransaction.getAmount());
    }

    @Override
    public void deleteAll() {
	transactionRepository.clear();
    }
}
