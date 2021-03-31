package com.zensar.controller;

import java.util.List;

import com.zensar.model.Payment;

public interface PaymentService {

	List<Payment> getAllByCardNumberAndMonthAndYearAndCvv(long cardNumber, String month, String year, int cvv);

}
