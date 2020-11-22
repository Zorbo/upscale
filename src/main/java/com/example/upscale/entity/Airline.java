package com.example.upscale.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * Airline entity
 *
 * @author tamas.kiss
 */
@Data
@Table(name = "airline")
@Entity
public class Airline {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "flights")
    @ElementCollection(targetClass = Flight.class)
    private Set<Flight> flights = new HashSet<>();

}
