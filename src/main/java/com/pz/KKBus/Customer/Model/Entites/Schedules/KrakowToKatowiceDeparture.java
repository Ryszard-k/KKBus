package com.pz.KKBus.Customer.Model.Entites.Schedules;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pz.KKBus.Staff.Model.Entites.Courses.Courses;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;

@Entity
public class KrakowToKatowiceDeparture implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime monFriDeparture;

    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime satSunDeparture;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "krakowToKatowice_id", nullable = false)
    private KrakowToKatowice krakowToKatowice;

    public KrakowToKatowiceDeparture() {
    }

    public KrakowToKatowiceDeparture(Long id, LocalTime monFriDeparture, LocalTime satSunDeparture, KrakowToKatowice krakowToKatowice) {
        this.id = id;
        this.monFriDeparture = monFriDeparture;
        this.satSunDeparture = satSunDeparture;
        this.krakowToKatowice = krakowToKatowice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getMonFriDeparture() {
        return monFriDeparture;
    }

    public void setMonFriDeparture(LocalTime monFriDeparture) {
        this.monFriDeparture = monFriDeparture;
    }

    public LocalTime getSatSunDeparture() {
        return satSunDeparture;
    }

    public void setSatSunDeparture(LocalTime satSunDeparture) {
        this.satSunDeparture = satSunDeparture;
    }

    public KrakowToKatowice getKrakowToKatowice() {
        return krakowToKatowice;
    }

    public void setKrakowToKatowice(KrakowToKatowice krakowToKatowice) {
        this.krakowToKatowice = krakowToKatowice;
    }

}
