package com.pz.KKBus.Staff.Model.Entites.Courses;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"report"})
public class StopPassengersPair {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String stop;
    private int passengers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    public StopPassengersPair(Long id, String stop, int passengers, Report report) {
        this.id = id;
        this.stop = stop;
        this.passengers = passengers;
        this.report = report;
    }

    public StopPassengersPair() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StopPassengersPair)) return false;
        StopPassengersPair that = (StopPassengersPair) o;
        return passengers == that.passengers && id.equals(that.id) && stop.equals(that.stop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stop, passengers);
    }
}
