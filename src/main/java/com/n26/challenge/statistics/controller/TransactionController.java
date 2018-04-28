package com.n26.challenge.statistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.n26.challenge.statistics.model.Transaction;
import com.n26.challenge.statistics.service.TransactionService;

/**
 * @author dsakho
 *
 */
@RestController
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping(value = "/transactions", headers = { "Accept=application/json" })
    public ResponseEntity<?> createTransaction(@RequestBody final Transaction transaction) {
	return transactionService.addTransaction(transaction);
    }

    @Autowired
    public void setTransactionService(final TransactionService transactionService) {
	this.transactionService = transactionService;
    }
}
