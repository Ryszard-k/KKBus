package com.pz.KKBus.Staff.Model.Entites.Courses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"amountOfPassengers"})
public class Report {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StopPassengersPair> amountOfPassengers;

    private int refuelingCost;

    private int distance;

    private int income;

    public Report(Long id, Set<StopPassengersPair> amountOfPassengers, int refuelingCost, int distance, int income) {
        this.id = id;
        this.amountOfPassengers = amountOfPassengers;
        this.refuelingCost = refuelingCost;
        this.distance = distance;
        this.income = income;
    }

    public Report() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<StopPassengersPair> getAmountOfPassengers() {
        return amountOfPassengers;
    }

    public void setAmountOfPassengers(Set<StopPassengersPair> amountOfPassengers) {
        this.amountOfPassengers = amountOfPassengers;
    }

    public int getRefuelingCost() {
        return refuelingCost;
    }

    public void setRefuelingCost(int refuelingCost) {
        this.refuelingCost = refuelingCost;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }
}
