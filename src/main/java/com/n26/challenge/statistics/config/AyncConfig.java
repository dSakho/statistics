package com.n26.challenge.statistics.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AyncConfig {

    @Bean(name = "transactionExecutor")
    public Executor threadPoolTaskExecutor() {
	return new ThreadPoolTaskExecutor();
    }

}
