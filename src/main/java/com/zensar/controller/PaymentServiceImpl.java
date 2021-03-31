package com.zensar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zensar.model.Payment;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	PaymentRepository repository;
	
	@Override
	public List<Payment> getAllByCardNumberAndMonthAndYearAndCvv(long cardNumber, String month, String year, int cvv) {
		return repository.queryByCardNumberEqualsAndMonthEqualsAndYearEqualsAndCvvEquals(cardNumber,month,year,cvv);
	}

}
