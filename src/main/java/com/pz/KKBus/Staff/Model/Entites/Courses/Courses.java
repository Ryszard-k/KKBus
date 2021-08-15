package com.pz.KKBus.Staff.Model.Entites.Courses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakowDeparture;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import com.pz.KKBus.Customer.Model.Enums.Route;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"katowiceToKrakowDeparture", "krakowToKatowiceDeparture"})
public class Courses {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private Route route;

    @NotNull
    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime departureTime;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employees driver;

    //onetone
    /*make reports*/

    public Courses(Long id, @NotNull LocalDate date, @NotNull Route route, @NotNull LocalTime departureTime, Car car, Employees driver) {
        this.id = id;
        this.date = date;
        this.route = route;
        this.departureTime = departureTime;
        this.car = car;
        this.driver = driver;
    }

    public Courses() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
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
        if (!(o instanceof Courses)) return false;
        Courses courses = (Courses) o;
        return id.equals(courses.id) && date.equals(courses.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }
}
