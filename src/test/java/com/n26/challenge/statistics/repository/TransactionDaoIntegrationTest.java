package com.n26.challenge.statistics.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.challenge.statistics.config.DaoConfig;
import com.n26.challenge.statistics.model.Transaction;

/**
 * @author dsakho
 *
 */
@Ignore
@RunWith(SpringRunner.class)
@Import(DaoConfig.class)
public class TransactionDaoIntegrationTest {

    @Autowired
    private TransactionDao transactionDao;

    @Before
    public void setUp() {
	transactionDao.deleteAll();
    }

    @Test
    public void assertAutowired() throws Exception {
	assertThat(transactionDao).isNotNull();
    }

    @Test
    public void testAddTransaction() throws Exception {
	final Transaction transaction = new Transaction(Instant.now().toEpochMilli(), new Double(12.3));

	transactionDao.addTransaction(transaction);

	assertThat(transactionDao.getAmountFromTimeStamp(transaction.getTimestamp())).isEqualTo(transaction.getAmount());
    }

    @Test
    public void testFindAll() throws Exception {
	final Transaction transactionSpyA = spy(new Transaction(Instant.parse("2017-04-27T10:15:30.00Z").toEpochMilli(), new Double(12.3)));
	final Transaction transactionSpyB = spy(new Transaction(Instant.now().toEpochMilli(), new Double(12.3)));

	transactionDao.addTransaction(transactionSpyA);
	transactionDao.addTransaction(transactionSpyB);

	final Collection<Transaction> transactions = transactionDao.findAll();

	assertThat(transactions).isNotNull().hasSize(2);
    }

    @Test
    public void testFindAllWithInDurationPeriod() throws Exception {
	final Duration fiveMinutes = Duration.ofMinutes(5L);
	final LocalDateTime now = LocalDateTime.now();

	final Transaction transactionSpyA =
		spy(new Transaction(now.minusMinutes(2).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(), 50.50d));
	transactionDao.addTransaction(transactionSpyA);

	final Transaction transactionSpyB =
		spy(new Transaction(now.minusMinutes(3).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(), 12.5d));
	transactionDao.addTransaction(transactionSpyB);

	final Transaction transactionSpyC =
		spy(new Transaction(now.minusMinutes(4).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(), 24.99d));
	transactionDao.addTransaction(transactionSpyC);

	final Transaction transactionSpyD =
		spy(new Transaction(now.minusMinutes(10).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(), 130.41d));
	transactionDao.addTransaction(transactionSpyD);

	final Transaction transactionSpyE =
		spy(new Transaction(now.minusMinutes(20).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(), 72.30d));
	transactionDao.addTransaction(transactionSpyE);

	final Collection<Transaction> transactionsLessThanDuration = transactionDao.findAllByDuration(fiveMinutes);

	assertThat(transactionsLessThanDuration).isNotNull().hasSize(3);
    }
}
