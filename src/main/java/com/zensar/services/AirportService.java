package com.zensar.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zensar.model.Airport;

public interface AirportService {
    public abstract Page<Airport> getAllAirportsPaged(int pageNum);
    public abstract List<Airport> getAllAirports();
    public abstract Airport getAirportById(Integer airportId);
    public abstract Airport saveAirport(Airport airport);
    public abstract void deleteAirport(Integer airpportId);
}
