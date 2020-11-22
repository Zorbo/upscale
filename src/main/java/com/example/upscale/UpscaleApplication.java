package com.example.upscale;

import com.example.upscale.service.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class UpscaleApplication implements CommandLineRunner {

    @Autowired
    private FlightService flightService;

    private static final Logger logger = LoggerFactory.getLogger(UpscaleApplication.class);

    public static void main(String[] args) {
       SpringApplication.run(UpscaleApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        flightService.createAirlines();
        System.out.println("PRINTED OUT MESSAGE: " + flightService.getAirlineList());
        System.out.println("\n");
        System.out.println("FLIGHTS USED BY AIRLINE WIZZ AIR : " + flightService.getFlightsByAirLines("Wizz Air"));
        System.out.println("\n");
        System.out.println("FLIGHTS BY DESTINATION BUDAPEST/LONDON: " + flightService.getFlightsByDestination("Budapest", "London"));
        System.out.println("\n");
        System.out.println("FLIGHTS FROM DESTINATION BUDAPEST: " + flightService.getFlightsByDestination("Budapest", null));
        System.out.println("\n");
        System.out.println("FLIGHTS TO DESTINATION LONDON: " + flightService.getFlightsByDestination(null, "London"));
        System.out.println("\n");
        System.out.println("SHORTEST FLIGHT DISTANCE OSLO TO LONDON " + flightService.getShortestFlightByAirlines("Ryan Air"));

//        System.out.println("FLIGHTS: " + flightService.getFlights());
//        System.out.println("CITIES: " + flightService.getCities());
        System.out.println("AIRLINES: " + flightService.getAirlines());
    }
}

