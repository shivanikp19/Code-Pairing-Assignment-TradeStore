package com.tradestorage.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tradestorage.controller.TradeController;
import com.tradestorage.exception.InvalidTradeException;
import com.tradestorage.model.Trade;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradestorageApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private TradeController tradeController;

	/**
	 * Tests if the given {@link Trade} instance is validated and saved into
	 * Database or not.
	 */
	@Test
	void testTradeValidateAndStoreSuccessful() {
		ResponseEntity<String> responseEntity = tradeController
				.validateAndSaveTrade(createTrade("Trade1", 1, LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);
		// List<Trade> tradeList = tradeController.getAllTrades();
		// Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("Trade1", tradeController.getTradeById("Trade1").get().getTradeId());
	}

	/**
	 * Tests the {@link TradeController#validateAndSaveTrade(Trade)} method when
	 * Maturity Date of given {@link Trade} is after the todays date. It should
	 * throw an exception.
	 */
	@Test
	void testTradeValidateAndStoreWhenMaturityDatePast() {
		try {
			LocalDate localDate = LocalDate.of(2015, 05, 21);
			tradeController.validateAndSaveTrade(createTrade("T2", 1, localDate));
		} catch (InvalidTradeException ie) {
			Assertions.assertEquals("Invalid Trade: T2 Trade Id is not found", ie.getMessage());
		}
	}

	/**
	 * Tests the {@link TradeController#validateAndSaveTrade(Trade)} method when
	 * version of given {@link Trade} is lower than the version present in the
	 * database.
	 */
	@Test
	void testValidateAndSaveTradeWithOldVersion() {
		// step-1 create trade
		ResponseEntity<String> responseEntity = tradeController
				.validateAndSaveTrade(createTrade("T1", 2, LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);
		List<Trade> tradeList = tradeController.getAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Trade trade = tradeController.getTradeById("T1").get();
		Assertions.assertEquals("T1", trade.getTradeId());
		Assertions.assertEquals(2, trade.getVersion());
		Assertions.assertEquals("T1B1", trade.getBookId());

		// step-2 create trade with old version
		try {
			tradeController.validateAndSaveTrade(createTrade("T1", 1, LocalDate.now()));

		} catch (InvalidTradeException e) {
			System.out.println(e.getId());
			System.out.println(e.getMessage());
		}
		List<Trade> tradeList1 = tradeController.getAllTrades();
		Assertions.assertEquals(1, tradeList1.size());
		Trade trade2 = tradeController.getTradeById("T1").get();
		Assertions.assertEquals("T1", trade2.getTradeId());
		Assertions.assertEquals(2, trade2.getVersion());
		Assertions.assertEquals("T1B1", trade2.getBookId());
	}

	/**
	 * Tests the {@link TradeController#validateAndSaveTrade(Trade)} method when
	 * version of given {@link Trade} is same as that of the version present in the
	 * database.
	 */
	@Test
	void testValidateAndSaveTradeWithSameVersionTrade() {
		ResponseEntity<String> responseEntity = tradeController
				.validateAndSaveTrade(createTrade("T1", 2, LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);
		List<Trade> tradeList = tradeController.getAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("T1", tradeList.get(0).getTradeId());
		Assertions.assertEquals(2, tradeList.get(0).getVersion());
		Assertions.assertEquals("T1B1", tradeList.get(0).getBookId());

		// step-2 create trade with same version
		Trade trade2 = createTrade("T1", 2, LocalDate.now());
		trade2.setBookId("T1B1V2");
		ResponseEntity<String> responseEntity2 = tradeController.validateAndSaveTrade(trade2);
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity2);
		List<Trade> tradeList2 = tradeController.getAllTrades();
		Assertions.assertEquals(1, tradeList2.size());
		Assertions.assertEquals("T1", tradeList2.get(0).getTradeId());
		Assertions.assertEquals(2, tradeList2.get(0).getVersion());
		Assertions.assertEquals("T1B1V2", tradeList2.get(0).getBookId());

		// step-2 create trade with new version
		Trade trade3 = createTrade("T1", 2, LocalDate.now());
		trade3.setBookId("T1B1V3");
		ResponseEntity<String> responseEntity3 = tradeController.validateAndSaveTrade(trade3);
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity3);
		List<Trade> tradeList3 = tradeController.getAllTrades();
		Assertions.assertEquals(1, tradeList3.size());
		Assertions.assertEquals("T1", tradeList3.get(0).getTradeId());
		Assertions.assertEquals(2, tradeList3.get(0).getVersion());
		Assertions.assertEquals("T1B1V3", tradeList3.get(0).getBookId());

	}

	/**
	 * Returns a new instance of {@link Trade} with the specified parameters are
	 * set.
	 * 
	 * @param tradeId
	 * @param version
	 * @param maturityDate
	 * 
	 * @return an instance of {@link Trade}
	 */
	private Trade createTrade(String tradeId, int version, LocalDate maturityDate) {
		Trade trade = new Trade();
		trade.setTradeId(tradeId);
		trade.setBookId(tradeId + "B1");
		trade.setVersion(version);
		trade.setCounterPartyId(tradeId + "Cpty");
		trade.setMaturityDate(maturityDate);
		trade.setExpired("N");
		return trade;
	}

}
