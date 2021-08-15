package com.pz.KKBus.Staff.Model.Entites.Courses;

import javax.persistence.*;

@Entity
public class StopPassengersPair {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String stop;
    private int passengers;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "report_id", nullable = false)
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
}
