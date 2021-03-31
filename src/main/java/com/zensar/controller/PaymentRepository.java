package com.zensar.controller;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zensar.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

	List<Payment> queryByCardNumberEqualsAndMonthEqualsAndYearEqualsAndCvvEquals(long cardNumber, String month, String year, int cvv);

}
