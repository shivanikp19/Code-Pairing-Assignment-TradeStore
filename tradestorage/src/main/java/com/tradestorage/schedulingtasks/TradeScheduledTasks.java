
package com.tradestorage.schedulingtasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tradestorage.service.TradeService;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TradeScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(TradeScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	TradeService tradeService;

	/**
	 * This method automatically updates an expire flag of trade if in a store the
	 * trade crosses the maturity date.
	 * 
	 */
	@Scheduled(cron = "${trade.expiry.schedule}") // scheduled the task to be executed every 30 seconds
	public void updateExpiryFlagOfTrade() {
		log.info("The time is now {}", dateFormat.format(new Date()));
		tradeService.updateExpiryFlagOfTrade();
	}
}