package com.n26.challenge.statistics.repository;

import java.time.Duration;
import java.util.Collection;

import com.n26.challenge.statistics.model.Transaction;

/**
 * @author dsakho
 *
 */
public interface TransactionDao {

    Collection<Transaction> findAll();

    Collection<Transaction> findAllByDuration(Duration timeDurection);

    Double getAmountFromTimeStamp(final Long timestamp);

    void addTransaction(final Transaction newTransaction);

    void deleteAll();
}
