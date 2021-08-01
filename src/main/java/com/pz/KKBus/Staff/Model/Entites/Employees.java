package com.pz.KKBus.Staff.Model.Entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pz.KKBus.Staff.Model.Enums.Role;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"availabilities", "unavailabilities", "schedules"})
public class Employees {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Role role;
    private int salary;

    @OneToMany(mappedBy = "employees", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Availability> availabilities;

    @OneToMany(mappedBy = "employeesUn", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Unavailability> unavailabilities;

    @OneToMany(mappedBy = "employeesSchedule", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Schedule> schedules;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CarProperties> carProperties;

    public Employees(){}

    public Employees(Long id, String firstName, String lastName, LocalDate birthDate, Role role, int salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.role = role;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Set<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(Set<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public Set<Unavailability> getUnavailabilities() {
        return unavailabilities;
    }

    public void setUnavailabilities(Set<Unavailability> unavailabilities) {
        this.unavailabilities = unavailabilities;
    }

    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }

    public Set<CarProperties> getCarProperties() {
        return carProperties;
    }

    public void setCarProperties(Set<CarProperties> carProperties) {
        this.carProperties = carProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employees)) return false;
        Employees employees = (Employees) o;
        return id.equals(employees.id) && firstName.equals(employees.firstName) && lastName.equals(employees.lastName) && birthDate.equals(employees.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthDate);
    }
}
