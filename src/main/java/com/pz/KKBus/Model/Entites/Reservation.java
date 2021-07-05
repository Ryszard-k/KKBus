package com.pz.KKBus.Model.Entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pz.KKBus.Model.Enums.Route;
import com.pz.KKBus.Model.Enums.Status;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @NotNull
    private int seats;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Route route;

    @NotNull
    private String fromStop;

    @NotNull
    private String toStop;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Autowired
    public Reservation(Long id, LocalDate date, LocalTime time, int seats, Route route, String fromStop, String toStop, Customer customer, Status status) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.seats = seats;
        this.route = route;
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.customer = customer;
        this.status = status;
    }

    public Reservation() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getFromStop() {
        return fromStop;
    }

    public void setFromStop(String fromStop) {
        this.fromStop = fromStop;
    }

    public String getToStop() {
        return toStop;
    }

    public void setToStop(String toStop) {
        this.toStop = toStop;
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

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getFrom() {
        return fromStop;
    }

    public void setFrom(String fromStop) {
        this.fromStop = fromStop;
    }

    public String getTo() {
        return toStop;
    }

    public void setTo(String toStop) {
        this.toStop = toStop;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


}
