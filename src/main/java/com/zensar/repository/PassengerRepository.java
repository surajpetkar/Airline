package com.zensar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zensar.model.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
