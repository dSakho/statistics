package com.n26.challenge.statistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.n26.challenge.statistics.service.StatisticsService;
import com.n26.challenge.statistics.view.StatisticsView;

/**
 * @author dsakho
 *
 */
@RestController
public class StatisticsController {

    private StatisticsService statisticsService;

    @GetMapping(value = "/statistics")
    public ResponseEntity<StatisticsView> viewTransactionStatistics() {
	return statisticsService.getStatistics();
    }

    @Autowired
    public void setStatisticsService(final StatisticsService statisticsService) {
	this.statisticsService = statisticsService;
    }
}
