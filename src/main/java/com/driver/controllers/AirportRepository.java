package com.driver.controllers;

import com.driver.misc.Solutions;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class AirportRepository {

    private HashMap<String , Airport> airportMap;
    private HashMap<Integer , Flight> flightMap;
    private HashMap<Integer , Passenger> passengerMap;
    private HashMap<Integer , List<Integer>> flightBookings;
    private HashMap<Integer , Integer> paymentMap;
    private HashMap<Integer , Integer> flightRevenueMap;
    private HashMap<String , Integer> airportFlightMap;

    AirportRepository(){
        this.airportMap = new HashMap<>();
        this.flightMap = new HashMap<>();
        this.passengerMap = new HashMap<>();
        this.flightBookings = new HashMap<>();
        this.paymentMap = new HashMap<>();
        this.flightRevenueMap = new HashMap<>();
        this.airportFlightMap = new HashMap<>();
    }

    //ADD AIRPORT
    public void addAirport(Airport airport) {
        airportMap.put(airport.getAirportName() , airport);
    }

    //ADD FLIGHT
    public void addFlight(Flight flight) {
        flightMap.put(flight.getFlightId() , flight);
        
    }

    //ADD PASSENGER
    public void addPassenger(Passenger passenger) {
        passengerMap.put(passenger.getPassengerId() , passenger);
    }

    public String getLargestAirportName() {
        String largestAirport = "";
        int max = 0; // Set max to the smallest possible value

        for(Airport currAirport : airportMap.values()){
            if(currAirport.getNoOfTerminals() > max || largestAirport.equals("")){
                max = currAirport.getNoOfTerminals();
                largestAirport = currAirport.getAirportName();
            } else if (currAirport.getNoOfTerminals() == max) {

                largestAirport = (largestAirport.compareTo(currAirport.getAirportName()) < 0) ? largestAirport : currAirport.getAirportName();
            }
        }
        return largestAirport;
    }


    public String bookATicket(Integer flightId, Integer passengerId) {
        int maxCapacity = flightMap.get(flightId).getMaxCapacity();
        if(flightBookings.containsKey(flightId) && flightBookings.get(flightId).size() == maxCapacity) return "FAILURE"; //Max CAP reached

        //Passenger has already booked a flight
        for(List<Integer> passengerList : flightBookings.values()){
            if(passengerList.contains(passengerId)) return "FAILURE";
        }
        //Update payment and flightrevenue maps
        int fare = calculateFare(flightId);
        paymentMap.put(passengerId , fare);
        fare += flightRevenueMap.getOrDefault(flightId,0);
        flightRevenueMap.put(flightId , fare);

        //Passenger is free to book a flight
        List<Integer> passengerList = flightBookings.getOrDefault(flightId , new ArrayList<>());
        passengerList.add(passengerId);
        flightBookings.put(flightId , passengerList);
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        if(!flightMap.containsKey(flightId)) return "FAILURE";

        List<Integer> passengerList = flightBookings.getOrDefault(flightId , new ArrayList<>());
        if(!passengerList.contains(passengerId)) return "FAILURE";

        //Update payment and flightrevenue maps
        int fare = paymentMap.getOrDefault(passengerId,0);
        paymentMap.remove(passengerId);
        int revenue = flightRevenueMap.get(flightId);
        flightRevenueMap.put(flightId , revenue - fare);

        //CANCEL BOOKING
        passengerList.remove(passengerId);
        flightBookings.put(flightId , passengerList);
        return "SUCCESS";
    }


    public String getAirportNameFromFlightId(Integer flightId) {
        if(!flightMap.containsKey(flightId)) return null;
        City city = flightMap.get(flightId).getFromCity();
        for(Airport airport : airportMap.values()){
            if(airport.getCity().equals(city)){
                return airport.getAirportName();
            }
        }
        return null;
    }

    public int calculateFare(Integer flightId) {
        int base = 3000;
        int alreadyBooked = 0;
        if(flightBookings.containsKey(flightId)){
            alreadyBooked = flightBookings.get(flightId).size();
        }
        return base + (alreadyBooked*50);
    }

    public double shortestDuration(City fromCity, City toCity) {
        double duration = Double.MAX_VALUE;
        for(Flight currFlight : flightMap.values()){
            if(currFlight.getFromCity() == fromCity && currFlight.getToCity() == toCity){
                if(duration == Double.MAX_VALUE){
                    duration = currFlight.getDuration();
                } else {
                    duration = Math.min(duration , currFlight.getDuration());
                }
            }
        }
        return duration == Double.MAX_VALUE ? -1 : duration;
    }

    public int getNumberOfPeople(Date date, String airportName) {
        City currCity = airportMap.get(airportName).getCity();
        int numberOfPeople = 0;
        for(Flight currFlight : flightMap.values()){
            if((currFlight.getToCity() == currCity || currFlight.getFromCity() == currCity) && currFlight.getFlightDate() == date){
                numberOfPeople += flightBookings.getOrDefault(currFlight , new ArrayList<>()).size();
            }
        }
        return numberOfPeople;
    }

    public int countOfBookings(Integer passengerId) {
        int count = 0;
        for(List<Integer> bookings : flightBookings.values()){
            if(bookings.contains(passengerId)) count++;
        }
        return count;
    }

    public int calculateRevenue(Integer flightId) {
        return flightRevenueMap.getOrDefault(flightId , 0);
    }
}
