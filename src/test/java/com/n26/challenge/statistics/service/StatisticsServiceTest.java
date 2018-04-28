package com.n26.challenge.statistics.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.challenge.statistics.model.Transaction;
import com.n26.challenge.statistics.repository.TransactionDao;
import com.n26.challenge.statistics.view.StatisticsView;

/**
 * @author dsakho
 *
 */
@RunWith(SpringRunner.class)
public class StatisticsServiceTest {

    private StatisticsService statisticsService;

    @MockBean
    private TransactionDao transactionDao;

    @Before
    public void setUp() {
	statisticsService = new StatisticsService();
	statisticsService.setTransactionDao(transactionDao);
    }

    @Test
    public void testGetStatisticsWithEmptyData() throws Exception {
	when(transactionDao.findAllByDuration(ArgumentMatchers.any(Duration.class))).thenReturn(Collections.emptyList());

	final ResponseEntity<StatisticsView> statsView = statisticsService.getStatistics();

	assertThat(statsView).isNotNull();
	assertThat(statsView.getBody()).isNotNull();
	assertThat(statsView.getBody().getCount()).isEqualTo(0);
	assertThat(statsView.getBody().getMin()).isEqualTo(0.0d);
	assertThat(statsView.getBody().getMax()).isEqualTo(0.0d);
	assertThat(statsView.getBody().getSum()).isEqualTo(0.0d);
	assertThat(statsView.getBody().getAvg()).isEqualTo(0.0d);
    }


    @Test
    public void testGetStatistics() throws Exception {
	final LocalDateTime now = LocalDateTime.now();

	final Transaction transactionSpyA =
		spy(new Transaction(now.minusSeconds(5).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(), 50.50d));
	final Transaction transactionSpyB =
		spy(new Transaction(now.minusSeconds(25).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(), 12.5d));
	final Transaction transactionSpyC =
		spy(new Transaction(now.minusSeconds(50).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(), 24.99d));
	final Transaction transactionSpyD =
		spy(new Transaction(now.minusSeconds(70).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(), 130.41d));
	final Transaction transactionSpyE =
		spy(new Transaction(now.minusSeconds(120).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli(), 72.30d));

	final Collection<Transaction> transactionCollection = Arrays.asList(transactionSpyA, transactionSpyB, transactionSpyC, transactionSpyD, transactionSpyE);

	when(transactionDao.findAllByDuration(ArgumentMatchers.any(Duration.class))).thenReturn(transactionCollection);

	final ResponseEntity<StatisticsView> statsView = statisticsService.getStatistics();

	assertThat(statsView).isNotNull();
	assertThat(statsView.getBody()).isNotNull();
	assertThat(statsView.getBody().getCount()).isEqualTo(5);
	assertThat(statsView.getBody().getMin()).isEqualTo(12.5d);
	assertThat(statsView.getBody().getMax()).isEqualTo(130.41d);

	System.out.println(statsView.getBody().toString());
    }
}
