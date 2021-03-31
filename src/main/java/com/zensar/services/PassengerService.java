package com.zensar.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zensar.model.Passenger;

public interface PassengerService {
    public abstract Page<Passenger> getAllPassengersPaged(int pageNum);
    public abstract List<Passenger> getAllPassengers();
    public abstract Passenger getPassengerById(Long passengerId);
    public abstract Passenger savePassenger(Passenger passenger);
    public abstract void deletePassengerById(Long passengerId);
}
