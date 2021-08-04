package com.pz.KKBus.Staff.Model.Entites;

import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakowDeparture;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowiceDeparture;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Courses {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "katowiceToKrakowDeparture_id")
    private KatowiceToKrakowDeparture katowiceToKrakowDeparture;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "krakowToKatowiceDeparture_id")
    private KrakowToKatowiceDeparture krakowToKatowiceDeparture;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employees driver;

    /*make reports*/

    public Courses(Long id, @NotNull LocalDate date, KatowiceToKrakowDeparture katowiceToKrakowDeparture, KrakowToKatowiceDeparture krakowToKatowiceDeparture, Car car, Employees driver) {
        this.id = id;
        this.date = date;
        this.katowiceToKrakowDeparture = katowiceToKrakowDeparture;
        this.krakowToKatowiceDeparture = krakowToKatowiceDeparture;
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

    public KatowiceToKrakowDeparture getKatowiceToKrakowDeparture() {
        return katowiceToKrakowDeparture;
    }

    public void setKatowiceToKrakowDeparture(KatowiceToKrakowDeparture katowiceToKrakowDeparture) {
        this.katowiceToKrakowDeparture = katowiceToKrakowDeparture;
    }

    public KrakowToKatowiceDeparture getKrakowToKatowiceDeparture() {
        return krakowToKatowiceDeparture;
    }

    public void setKrakowToKatowiceDeparture(KrakowToKatowiceDeparture krakowToKatowiceDeparture) {
        this.krakowToKatowiceDeparture = krakowToKatowiceDeparture;
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
