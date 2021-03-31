package com.zensar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zensar.model.Aircraft;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
}
