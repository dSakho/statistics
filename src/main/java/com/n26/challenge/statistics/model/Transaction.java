package com.n26.challenge.statistics.model;

import lombok.Data;

/**
 * @author dsakho
 *
 */
@Data
public class Transaction {

    private Long timestamp = 0l;
    private Double amount = 0.0d;

    public Transaction() { }

    public Transaction(final Long timestamp, final Double amount) {
	this.timestamp = timestamp;
	this.amount = amount;
    }
}
