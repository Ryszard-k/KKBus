package com.pz.KKBus.Customer.Model.Entites.Schedules;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class KatowiceToKrakow implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String stop;

    @OneToMany(mappedBy = "katowiceToKrakow", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<KatowiceToKrakowDeparture> katowiceToKrakowDeparture;

    private Integer price;

    private Integer distance;

    public KatowiceToKrakow(Long id, String stop, Set<KatowiceToKrakowDeparture> katowiceToKrakowDeparture, Integer price, Integer distance) {
        this.id = id;
        this.stop = stop;
        this.katowiceToKrakowDeparture = katowiceToKrakowDeparture;
        this.price = price;
        this.distance = distance;
    }

    public KatowiceToKrakow() {
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Set<KatowiceToKrakowDeparture> getKatowiceToKrakowDeparture() {
        return katowiceToKrakowDeparture;
    }

    public void setKatowiceToKrakowDeparture(Set<KatowiceToKrakowDeparture> katowiceToKrakowDeparture) {
        this.katowiceToKrakowDeparture = katowiceToKrakowDeparture;
    }
}
