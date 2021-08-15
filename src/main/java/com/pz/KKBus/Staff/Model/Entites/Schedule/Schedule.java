package com.pz.KKBus.Staff.Model.Entites.Schedule;

import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate workDate;

    @NotNull
    private LocalTime fromWorkTime;

    @NotNull
    private LocalTime toWorkTime;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employees employeesSchedule;

    public Schedule(Long id, LocalDate workDate, LocalTime fromWorkTime, LocalTime toWorkTime, Employees employeesSchedule) {
        this.id = id;
        this.workDate = workDate;
        this.fromWorkTime = fromWorkTime;
        this.toWorkTime = toWorkTime;
        this.employeesSchedule = employeesSchedule;
    }

    public Schedule() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public LocalTime getFromWorkTime() {
        return fromWorkTime;
    }

    public void setFromWorkTime(LocalTime fromWorkTime) {
        this.fromWorkTime = fromWorkTime;
    }

    public LocalTime getToWorkTime() {
        return toWorkTime;
    }

    public void setToWorkTime(LocalTime toWorkTime) {
        this.toWorkTime = toWorkTime;
    }

    public Employees getEmployeesSchedule() {
        return employeesSchedule;
    }

    public void setEmployeesSchedule(Employees employeesSchedule) {
        this.employeesSchedule = employeesSchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Schedule)) return false;
        Schedule schedule = (Schedule) o;
        return workDate.equals(schedule.workDate) && fromWorkTime.equals(schedule.fromWorkTime) && toWorkTime.equals(schedule.toWorkTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workDate, fromWorkTime, toWorkTime);
    }
}
