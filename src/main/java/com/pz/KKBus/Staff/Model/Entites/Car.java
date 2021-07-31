package com.pz.KKBus.Staff.Model.Entites;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String brand;

    @NotNull
    private String model;

    @NotNull
    private int seats;

    @NotNull
    private LocalDate manufactureYear;

    public Car(Long id, String brand, String model, int seats, LocalDate manufactureYear) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.seats = seats;
        this.manufactureYear = manufactureYear;
    }

    public Car() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public LocalDate getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(LocalDate manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return id.equals(car.id) && brand.equals(car.brand) && model.equals(car.model) && manufactureYear.equals(car.manufactureYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, manufactureYear);
    }
}