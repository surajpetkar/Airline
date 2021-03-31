package com.zensar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zensar.model.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Integer> {
}
