package com.tradestorage.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tradestorage.repository.TradeRepository;
import com.tradestorage.model.Trade;

@Service
public class TradeService {

	private static final Logger log = LoggerFactory.getLogger(TradeService.class);

	@Autowired
	TradeRepository tradeRepository;

	/**
	 * This method validates the Maturity Date and version of the given
	 * {@link Trade} instance.
	 * 
	 * @param trade The instance of {@link Trade}.
	 * 
	 * @return True if the Maturity Date is after the today's date and version is
	 *         correct otherwise returns false.
	 */
	public boolean isValid(Trade trade) {
		if (validateTradeMaturityDate(trade)) {
			Optional<Trade> exsitingTrade = tradeRepository.findById(trade.getTradeId());
			if (exsitingTrade.isPresent()) {
				return validateTradeVersion(trade, exsitingTrade.get());
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method validates the version of given {@link Trade} instance with the
	 * same existing {@link Trade} instance in database. If the version is same it
	 * will override the existing record.
	 * 
	 * @param trade    The instance of {@link Trade}.
	 * @param oldTrade The same existing {@link Trade} instance in database.
	 * @return true if the version of given {@link Trade} is higher than the same
	 *         existing instance in database otherwise returns false.
	 */
	private boolean validateTradeVersion(Trade trade, Trade oldTrade) {
		if (trade.getVersion() >= oldTrade.getVersion()) {
			return true;
		}
		return false;
	}

	/**
	 * This method validates the Maturity Date of given {@link Trade} instance with
	 * the today's date.
	 * 
	 * @param trade The instance of {@link Trade}.
	 * 
	 * @return true if the Maturity Date of given {@link Trade} is after the today's
	 *         date otherwise returns false.
	 */
	private boolean validateTradeMaturityDate(Trade trade) {
		return trade.getMaturityDate().isBefore(LocalDate.now()) ? false : true;
	}

	/**
	 * <p>
	 * This method updates Expired value of the given {@link Trade} instance into
	 * the database based on the maturity date of trade.
	 * </p>
	 * <p>
	 * It will set the Expired value to <code>Y</code> if in a store the trade
	 * crosses the maturity date otherwise it is set to <code>N</code>.
	 * </p>
	 */
	public void updateExpiryFlagOfTrade() {
		tradeRepository.findAll().stream().forEach(trade -> {
			if (!validateTradeMaturityDate(trade)) {
				trade.setExpired("Y");
				log.info("Trade which needs to be updated {}", trade);
				tradeRepository.save(trade);
			}
		});
	}

	/**
	 * Saves the given{@link Trade} data to database.
	 * 
	 * @param trade The instance of {@link Trade}.
	 */
	public void saveTrade(Trade trade) {
		trade.setCreatedDate(LocalDate.now());
		tradeRepository.save(trade);
	}

	/**
	 * Returns all instances of the type {@link Trade} from the database.
	 * 
	 * @return Returns all instances of the {@link Trade}.
	 */
	public List<Trade> getAllTrades() {
		return tradeRepository.findAll();
	}

	/**
	 * Returns an instance of the type {@link Trade} from the database based on the
	 * given <code>tradeId</code>.
	 * 
	 * @return Returns an instance of the {@link Trade}.
	 */
	public Optional<Trade> getTradeById(String tradeId) {
		return tradeRepository.findById(tradeId);
	}

}
