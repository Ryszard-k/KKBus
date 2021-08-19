package com.pz.KKBus.Customer.Model.Entites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.pz.KKBus.Customer.Model.Enums.RewardStatus;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"customerReward"})
public class Reward {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int points;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customerReward;

    @Enumerated(EnumType.STRING)
    private RewardStatus rewardStatus;

    public Reward(Long id, String name, int points, LocalDate date, Customer customerReward, RewardStatus rewardStatus) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.date = date;
        this.customerReward = customerReward;
        this.rewardStatus = rewardStatus;
    }

    public Reward() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customerReward;
    }

    public void setCustomer(Customer customerReward) {
        this.customerReward = customerReward;
    }

    public RewardStatus getRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(RewardStatus rewardStatus) {
        this.rewardStatus = rewardStatus;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reward)) return false;
        Reward reward = (Reward) o;
        return id.equals(reward.id) && name.equals(reward.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
