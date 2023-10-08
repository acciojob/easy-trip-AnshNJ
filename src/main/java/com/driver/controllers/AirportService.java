package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AirportService {

    AirportRepository airportRepo = new AirportRepository();
    public void addAirport(Airport airport) {
        airportRepo.addAirport(airport);
    }

    public String getLargestAirportName() {
        return airportRepo.getLargestAirportName();
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        return airportRepo.shortestDuration(fromCity , toCity);
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        return airportRepo.getNumberOfPeople(date , airportName);
    }

    public int calculateFlightFare(Integer flightId) {
        return airportRepo.calculateFare(flightId);
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        return airportRepo.bookATicket(flightId , passengerId);
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        return airportRepo.cancelATicket(flightId , passengerId);
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        return airportRepo.countOfBookings(passengerId);
    }

    public String addFlight(Flight flight) {
        airportRepo.addFlight(flight);
        return "SUCCESS";
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        return airportRepo.getAirportNameFromFlightId(flightId);
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        return airportRepo.calculateRevenue(flightId);
    }

    public String addPassenger(Passenger passenger) {
        airportRepo.addPassenger(passenger);
        return "SUCCESS";
    }
}
