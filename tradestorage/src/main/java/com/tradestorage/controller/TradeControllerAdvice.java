package com.tradestorage.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tradestorage.exception.CustomErrorDetails;
import com.tradestorage.exception.InvalidTradeException;

@ControllerAdvice
public class TradeControllerAdvice extends ResponseEntityExceptionHandler {
	@ExceptionHandler(InvalidTradeException.class)
	public ResponseEntity<Object> handleInvalidTradeException(final InvalidTradeException e, WebRequest request) {
		return error(e, HttpStatus.NOT_ACCEPTABLE, request);
	}

	private ResponseEntity<Object> error(final Exception exception, final HttpStatus httpStatus,
			final WebRequest request) {
		final String message = Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());

		CustomErrorDetails customErrorDetails = new CustomErrorDetails(new Date(), message,
				request.getDescription(false));
		return new ResponseEntity<>(customErrorDetails, httpStatus);
	}

}
