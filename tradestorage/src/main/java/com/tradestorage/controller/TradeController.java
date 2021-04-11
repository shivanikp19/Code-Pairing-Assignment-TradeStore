package com.tradestorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tradestorage.exception.InvalidTradeException;
import com.tradestorage.model.Trade;
import com.tradestorage.service.TradeService;

import java.util.List;
import java.util.Optional;

@RestController
public class TradeController {
	@Autowired
	TradeService tradeService;

	@PostMapping("/trades")
	public ResponseEntity<String> validateAndSaveTrade(@RequestBody Trade trade) {
		if (tradeService.isValid(trade)) {
			tradeService.saveTrade(trade);
		} else {
			// return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			throw new InvalidTradeException(trade.getTradeId() + " Trade Id is not found");
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/trades")
	public List<Trade> getAllTrades() {
		return tradeService.getAllTrades();
	}
	
	@GetMapping("/trades/{id}")
	public Optional<Trade> getTradeById(@PathVariable("id") String tradeId) {
		return tradeService.getTradeById(tradeId);
	}
	
}
