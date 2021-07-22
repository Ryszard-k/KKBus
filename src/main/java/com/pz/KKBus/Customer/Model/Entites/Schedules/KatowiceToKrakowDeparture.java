package com.pz.KKBus.Customer.Model.Entites.Schedules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;

@Entity
public class KatowiceToKrakowDeparture implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime monFriDeparture;

    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime satSunDeparture;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "krakowToKatowice_id", nullable = false)
    @JsonIgnore
    private KatowiceToKrakow katowiceToKrakow;

    public KatowiceToKrakowDeparture() {
    }

    public KatowiceToKrakowDeparture(Long id, LocalTime monFriDeparture, LocalTime satSunDeparture, KatowiceToKrakow katowiceToKrakow) {
        this.id = id;
        this.monFriDeparture = monFriDeparture;
        this.satSunDeparture = satSunDeparture;
        this.katowiceToKrakow = katowiceToKrakow;
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

    public KatowiceToKrakow getKatowiceToKrakow() {
        return katowiceToKrakow;
    }

    public void setKatowiceToKrakow(KatowiceToKrakow katowiceToKrakow) {
        this.katowiceToKrakow = katowiceToKrakow;
    }
}
