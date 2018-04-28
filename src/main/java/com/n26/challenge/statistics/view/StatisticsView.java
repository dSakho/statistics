package com.n26.challenge.statistics.view;

import java.io.Serializable;

import lombok.Data;

@Data
public final class StatisticsView implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double sum;
    private Double avg;
    private Double max;
    private Double min;
    private Long count;
}
