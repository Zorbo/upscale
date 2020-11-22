package com.example.upscale.service;

import com.example.upscale.entity.Airline;
import com.example.upscale.entity.City;
import com.example.upscale.entity.Flight;
import com.example.upscale.exceptions.AirlineException;
import com.example.upscale.repository.AirlineRepository;
import com.example.upscale.repository.CityRepository;
import com.example.upscale.repository.FlightRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import lombok.Data;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class responsible to create flight logic
 *
 * @author tamas.kiss
 */
@Data
@Service
@ToString
public class FlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);
    public static final String OSLO = "Oslo";
    public static final String LONDON = "London";
    public static final String BUDAPEST = "Budapest";
    public static final String BERLIN = "Berlin";
    public static final String ROME = "Rome";

    private AirlineRepository airlineRepository;
    private FlightRepository flightRepository;
    private CityRepository cityRepository;
    private Random ran = new Random();
    private List<Airline> airlineList = new ArrayList<>();
    private List<Flight> flights = new ArrayList<>();


    @Autowired
    public FlightService(AirlineRepository airlineRepository,
                         FlightRepository flightRepository,
                         CityRepository cityRepository) {
        this.airlineRepository = airlineRepository;
        this.flightRepository = flightRepository;
        this.cityRepository = cityRepository;
    }

    /**
     * Filter the flights by airlines
     *
     * @param airLineName the specific airline name
     * @return the filtered flights
     */
    public Set<Flight> getFlightsByAirLines(String airLineName) {
        return airlineList
            .stream()
            .filter(airline -> airLineName.equalsIgnoreCase(airline.getName()))
            .findFirst()
            .orElseThrow(() -> new AirlineException("AIRLINE IS NOT EXISTING: " + airLineName))
            .getFlights();
    }

    /**
     * Get flights by destinations
     *
     * @param cityFrom the first city parameter
     * @param cityTo the second city parameter
     * @return the filtered flights by city
     */
    public Map<String, String> getFlightsByDestination(String cityFrom, String cityTo) {

        Map<String, String> filteredFlights = new HashMap<>();
        for (Flight flight : flights) {
            for (Map.Entry<City, City> entry : flight.getDestinations().entrySet()) {
                if (entry.getKey().getName().equalsIgnoreCase(cityFrom)
                    && entry.getValue().getName().equalsIgnoreCase(cityTo)) {
                    filteredFlights.put("Flight: " + flight.getName(), "From: " + cityFrom + " | To: " + cityTo);
                }
                if (entry.getKey().getName().equalsIgnoreCase(cityFrom) && cityTo == null) {
                    filteredFlights.put("Flight: " + flight.getName(),
                                        "From: " + cityFrom + " | To: " + entry.getValue().getName());
                }
                if (entry.getValue().getName().equalsIgnoreCase(cityTo) && cityFrom == null) {
                    filteredFlights.put("Flight: " + flight.getName(),
                                        "From: " + entry.getKey().getName() + " | To: " + cityTo);
                }
            }
        }
        return filteredFlights;
    }

    /**
     * Get the shortest flight by some specifications
     *
     * @param airline the specific airline
     * @return the result
     */
    public Map<String, String> getShortestFlightByAirlines(String airline) {
        Map<String, String> result = new HashMap<>();
        for (Flight flight : getFlightsByAirLines(airline)) {
            for (Map.Entry<City, City> entry : flight.getDestinations().entrySet()) {
                if (entry.getKey().getName().equals(OSLO) && entry.getValue().getName().equals(LONDON)) {
                    result.put("With " + airline + " the distance Oslo to London is:",
                               "" + flight.getDistance() + "km");
                } else {
                    result.put(airline + " don't have any direct flight ", OSLO + " to " + LONDON);
                }
                return result;
            }
        }
        return result;
    }

    /**
     * Create destinations for flights
     *
     * @return two random destination
     */
    // Note: Map cause one flight could have more destinations
    public Map<City, City> createDestinations() {
        Map<City, City> destinations = new HashMap<>();
        List<City> cities = getCities();
        while (destinations.isEmpty()) {
            int randomCityFrom = ran.nextInt(cityRepository.findAll().size());
            int randomCityTo = ran.nextInt(cityRepository.findAll().size());
            if (randomCityFrom != randomCityTo) {
                destinations.put(cities.get(randomCityFrom), cities.get(randomCityTo));
            }
        }
        return destinations;
    }

    /**
     * Get all the airlines
     *
     * @return the airline list
     */
    public List<Airline> getAirlines() {
        airlineList = airlineRepository.findAll();
        airlineList.forEach(f -> f.setFlights(createFlights()));
        return airlineList;
    }

    /**
     * Get all the flights
     *
     * @return the flight list
     */
    public List<Flight> getFlights() {
        flights = flightRepository.findAll();
        flights.forEach(f -> f.setDestinations(createDestinations()));

        for (Flight flight : flights) {
            List<String> fromTo = new ArrayList<>();
            for (Map.Entry<City, City> entry : flight.getDestinations().entrySet()) {
                fromTo.add(entry.getKey().getName());
                fromTo.add(entry.getValue().getName());
            }

            if (fromTo.contains(BUDAPEST) && fromTo.contains(LONDON)) {
                flight.setDistance(1449);
            }
            if (fromTo.contains(BUDAPEST) && fromTo.contains(BERLIN)) {
                flight.setDistance(688);
            }
            if (fromTo.contains(BUDAPEST) && fromTo.contains(OSLO)) {
                flight.setDistance(1482);
            }
            if (fromTo.contains(BUDAPEST) && fromTo.contains(ROME)) {
                flight.setDistance(809);
            }
            //---------------------------------
            if (fromTo.contains(LONDON) && fromTo.contains(BERLIN)) {
                flight.setDistance(932);
            }
            if (fromTo.contains(LONDON) && fromTo.contains(OSLO)) {
                flight.setDistance(1152);
            }
            if (fromTo.contains(LONDON) && fromTo.contains(ROME)) {
                flight.setDistance(1435);
            }
            //------------------------
            if (fromTo.contains(BERLIN) && fromTo.contains(OSLO)) {
                flight.setDistance(838);
            }
            if (fromTo.contains(BERLIN) && fromTo.contains(ROME)) {
                flight.setDistance(1184);
            }
            //---------------------
            if (fromTo.contains(OSLO) && fromTo.contains(ROME)) {
                flight.setDistance(2007);
            }
        }
        return flights;
    }

    /**
     * Get all the cities
     *
     * @return the city list
     */
    public List<City> getCities() {
        return cityRepository.findAll();
    }

    /**
     * Create the airlines
     */
    public void createAirlines() {
        airlineList = airlineRepository.findAll();
        airlineList.forEach(a -> a.setFlights(createFlights()));
    }

    /**
     * Create unique flights with destinations
     *
     * @return the flights with destinations
     */
    private Set<Flight> createFlights() {
        Set<Flight> fourUniqueFlights = new HashSet<>();
        List<Flight> flights = getFlights();
        flights.forEach(f -> f.setDestinations(createDestinations()));
        while (fourUniqueFlights.size() != 4) {
            int randomFlights = ran.nextInt(flightRepository.findAll().size());
            fourUniqueFlights.add(flights.get(randomFlights));
        }
        return fourUniqueFlights;
    }

}
