package com.pz.KKBus.Staff.Model.Entites;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Unavailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employees employeesUn;

    @Autowired
    public Unavailability(Long id, LocalDate date, Employees employeesUn) {
        this.id = id;
        this.date = date;
        this.employeesUn = employeesUn;
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

    public Employees getEmployeesUn() {
        return employeesUn;
    }

    public void setEmployeesUn(Employees employeesUn) {
        this.employeesUn = employeesUn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unavailability)) return false;
        Unavailability that = (Unavailability) o;
        return id.equals(that.id) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }
}
