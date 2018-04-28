package com.n26.challenge.statistics.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.n26.challenge.statistics.service.StatisticsService;
import com.n26.challenge.statistics.view.StatisticsView;

/**
 * @author dsakho
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;

    @Test
    public void testGetStatistics() throws Exception {
	final StatisticsView statsView = new StatisticsView();

	statsView.setCount(2L);
	statsView.setAvg(150.0d);
	statsView.setMax(200.0d);
	statsView.setMin(50.0d);
	statsView.setSum(250.0d);

	when(statisticsService.getStatistics()).thenReturn(ResponseEntity.ok().body(statsView));

	final MockHttpServletResponse response =
		mockMvc.perform(get("/statistics"))
		.andExpect(status().isOk())
		.andDo(print())
		.andReturn()
		.getResponse();

	assertThat(response).isNotNull();
	assertThat(response.getContentAsString()).isNotNull();
    }
}
