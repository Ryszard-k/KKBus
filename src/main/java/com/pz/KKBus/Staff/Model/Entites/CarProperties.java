package com.pz.KKBus.Staff.Model.Entites;

import com.pz.KKBus.Staff.Model.Enums.State;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class CarProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "car", nullable = false)
    private Car car;

    @NotNull
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private State state;

    /*
    toDo
    change to entity type
     */
    private String parking;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "driver", nullable = false)
    private Employees driver;

    public CarProperties(Long id, Car car, @NotNull LocalDate date, State state, String parking) {
        this.id = id;
        this.car = car;
        this.date = date;
        this.state = state;
        this.parking = parking;
    }

    public CarProperties() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public Employees getDriver() {
        return driver;
    }

    public void setDriver(Employees driver) {
        this.driver = driver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarProperties)) return false;
        CarProperties that = (CarProperties) o;
        return id.equals(that.id) && car.equals(that.car) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, car, date);
    }
}
