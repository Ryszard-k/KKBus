package com.pz.KKBus.Staff.Model.Entites.Schedule;

import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employees employees;

    @Autowired
    public Availability(Long id, LocalDate date, Employees employees) {
        this.id = id;
        this.date = date;
        this.employees = employees;
    }

    public Availability() {
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

    public Employees getEmployees() {
        return employees;
    }

    public void setEmployees(Employees employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability)) return false;
        Availability that = (Availability) o;
        return id.equals(that.id) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }
}
