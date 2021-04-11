package com.tradestorage.tests.schedulingtasks;

import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.tradestorage.TradeStorageApplication;
import com.tradestorage.schedulingtasks.TradeScheduledTasks;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig(TradeStorageApplication.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradeScheduledTasksTest {

	@SpyBean
	private TradeScheduledTasks tradeScheduledTasks;

	@Test
	public void whenWaitOneSecondThenScheduledIsCalledAtLeastTenTimes() {
		await().atMost(Durations.ONE_MINUTE)
				.untilAsserted(() -> verify(tradeScheduledTasks, atLeast(2)).updateExpiryFlagOfTrade());
	}

}