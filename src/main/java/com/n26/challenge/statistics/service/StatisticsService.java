package com.n26.challenge.statistics.service;

import java.time.Duration;
import java.util.Collection;
import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.n26.challenge.statistics.model.Transaction;
import com.n26.challenge.statistics.repository.TransactionDao;
import com.n26.challenge.statistics.view.StatisticsView;

/**
 * @author dsakho
 *
 */
@Service
public class StatisticsService {

    private static final DoubleSupplier ZERO_AS_DOUBLE = () -> {
	return 0.0d;
    };

    private static final ToDoubleFunction<Transaction> TRANSACTION_AMOUNT = Transaction::getAmount;

    private TransactionDao transactionDao;

    /**
     * @return
     */
    public ResponseEntity<StatisticsView> getStatistics() {
	final StatisticsView statsView = new StatisticsView();

	final Duration timeDuration = Duration.ofSeconds(60);
	final Collection<Transaction> transactionStream = transactionDao.findAllByDuration(timeDuration);

	// sum
	statsView.setSum(transactionStream.stream().mapToDouble(TRANSACTION_AMOUNT).sum());

	// average
	statsView.setAvg(transactionStream.stream().mapToDouble(TRANSACTION_AMOUNT).average().orElseGet(ZERO_AS_DOUBLE));

	// maximum
	statsView.setMax(transactionStream.stream().mapToDouble(TRANSACTION_AMOUNT).max().orElseGet(ZERO_AS_DOUBLE));

	// minimum
	statsView.setMin(transactionStream.stream().mapToDouble(TRANSACTION_AMOUNT).min().orElseGet(ZERO_AS_DOUBLE));

	// count
	statsView.setCount(transactionStream.stream().count());

	return ResponseEntity.ok().body(statsView);
    }

    @Autowired
    public void setTransactionDao(final TransactionDao transactionDao) {
	this.transactionDao = transactionDao;
    }
}
