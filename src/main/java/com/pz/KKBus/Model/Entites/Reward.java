package com.pz.KKBus.Model.Entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pz.KKBus.Model.Enums.RewardStatus;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Reward {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @NotNull
    private String name;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private RewardStatus rewardStatus;

    public Reward(Long id, String name, LocalDate date, Customer customer, RewardStatus rewardStatus) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.customer = customer;
        this.rewardStatus = rewardStatus;
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
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public RewardStatus getRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(RewardStatus rewardStatus) {
        this.rewardStatus = rewardStatus;
    }
}
