package com.pz.KKBus.Staff.Model.Entites.Courses;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
//@JsonIgnoreProperties({"amountOfPassengers"})
public class Report {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(name = "amountOfPassengers")
    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StopPassengersPair> amountOfPassengers;

    private int refuelingCost;

    private int distance;

    private int income;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "Course_id", nullable = false)
    private Courses courses;

    public Report(Long id, Set<StopPassengersPair> amountOfPassengers, int refuelingCost, int distance, int income, Courses courses) {
        this.id = id;
        this.amountOfPassengers = amountOfPassengers;
        this.refuelingCost = refuelingCost;
        this.distance = distance;
        this.income = income;
        this.courses = courses;
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

    public Courses getCourses() {
        return courses;
    }

    public void setCourses(Courses courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report)) return false;
        Report report = (Report) o;
        return id.equals(report.id) && amountOfPassengers.equals(report.amountOfPassengers) && courses.equals(report.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amountOfPassengers, courses);
    }
}
