package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.*;

public class AirportRepository {
    private TreeMap<String,Airport> airportMap= new TreeMap<>();
    private HashMap<Integer,Flight> flightMap= new HashMap<>();
    private HashMap<Integer,Passenger> passengerMap= new HashMap<>();
    private HashMap<Integer,Set<Integer>> flightBookings= new HashMap<>();
    private HashMap<Integer,Integer> revenueMap= new HashMap<>();
    private HashMap<Integer,Integer> paymentMap= new HashMap<>();

    public void addAirport(Airport airport) {

        airportMap.put(airport.getAirportName(),airport);
    }

    public String getLargestAirportName() {
        String largestAirport="";
        int max=0;
        for(String name : airportMap.keySet()){
            int currMax=airportMap.get(name).getNoOfTerminals();
            if(currMax > max){
                max = currMax;
                largestAirport = name;
            }
        }
        return largestAirport;
    }

    public void addFlight(Flight flight) {
        flightMap.put(flight.getFlightId(),flight);
    }

    public void addPassenger(Passenger passenger) {
        passengerMap.put(passenger.getPassengerId(),passenger);
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        Flight flight=flightMap.get(flightId);
        int maxcapacity=flight.getMaxCapacity();

        Set<Integer> list= new HashSet<>();
        if(flightBookings.containsKey(flightId)){
            list=flightBookings.get(flightId);
        }

        int capacity=list.size();
        if(capacity==maxcapacity) return "FAILURE";
        else if(list.contains(passengerId)) return "FAILURE";

        //Update payment and revenue maps
        int fare=calculateFare(flightId);
        paymentMap.put(passengerId,fare);
        fare+=revenueMap.getOrDefault(flightId,0);
        revenueMap.put(flightId,fare);

        //update booking map
        list.add(passengerId);
        flightBookings.put(flightId,list);
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        Set<Integer> list= flightBookings.get(flightId);

        if(list.contains(passengerId)){
            list.remove(passengerId);

            int fare=paymentMap.getOrDefault(passengerId,0);
            paymentMap.remove(passengerId);

            int revenue=revenueMap.getOrDefault(flightId,0);
            revenueMap.put(flightId,revenue-fare);

            flightBookings.put(flightId , list); //---
            return "SUCCESS";
        }
        return "FAILURE";
    }

    public int countOfBookings(Integer passengerId) {
        int count=0;
        for(Integer flightId:flightBookings.keySet()){
            Set<Integer> list=flightBookings.get(flightId);
            if(list.contains(passengerId)){
                count++;
            }
        }
        return count;
    }

    public int calculateFare(Integer flightId) {
        int fare=3000;
        int alreadyBooked=0;
        if(flightBookings.containsKey(flightId))
            alreadyBooked=flightBookings.get(flightId).size();
        return (fare+(alreadyBooked*50));
    }

    public double shortestDuration(City fromCity, City toCity) {
        double duration=Integer.MAX_VALUE;
        for (Flight flight :flightMap.values()){
            if(fromCity.equals(flight.getFromCity()) && toCity.equals(flight.getToCity())){
                if(duration>flight.getDuration()){
                    duration=flight.getDuration();
                }
            }
        }
        return duration==Integer.MAX_VALUE?-1:duration;
    }

    public int calculateRevenue(Integer flightId) {
        Integer revenue= revenueMap.getOrDefault(flightId,0);
        return revenue;
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        if(!flightMap.containsKey(flightId)) return null;
        Flight flight= flightMap.get(flightId);
        City city=flight.getFromCity();
        for (String airportname:airportMap.keySet()){
            Airport airport=airportMap.get(airportname);
            if(city.equals(airport.getCity())){
                return airportname;
            }
        }
        return null;
    }

    public int getNumberOfPeople(Date date, String airportName) {
        Airport airport=airportMap.get(airportName);
        int count=0;
        if(airport!=null){
            City city=airport.getCity();
            for(Flight flight : flightMap.values()){
                if(date.equals(flight.getFlightDate())){
                    if(city.equals(flight.getToCity()) || city.equals(flight.getFromCity())){
                        Integer flightId=flight.getFlightId();
                        Set<Integer> list=flightBookings.get(flightId);
                        if(list!=null){
                            count+= list.size();
                        }
                    }
                }
            }}
        return count;
    }
}