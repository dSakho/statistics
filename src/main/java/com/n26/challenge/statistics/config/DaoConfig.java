package com.n26.challenge.statistics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.n26.challenge.statistics.repository.TransactionDao;
import com.n26.challenge.statistics.repository.TransactionDaoImpl;

/**
 * @author dsakho
 *
 */
@Configuration
public class DaoConfig {

    @Bean
    TransactionDao transactionDao() {
	return new TransactionDaoImpl();
    }
}
