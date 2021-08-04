package com.pz.KKBus.Staff.Model.Entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"carProperties"})
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

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CarProperties> carProperties;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Courses> courses;

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

    public Set<CarProperties> getCarProperties() {
        return carProperties;
    }

    public void setCarProperties(Set<CarProperties> carProperties) {
        this.carProperties = carProperties;
    }

    public Set<Courses> getCourses() {
        return courses;
    }

    public void setCourses(Set<Courses> courses) {
        this.courses = courses;
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
