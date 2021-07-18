package com.pz.KKBus.Manager;

import com.pz.KKBus.Model.Entites.Customer;
import com.pz.KKBus.Model.Entites.Reservation;
import com.pz.KKBus.Model.Entites.Reward;
import com.pz.KKBus.Model.Enums.RewardStatus;
import com.pz.KKBus.Model.Enums.Status;
import com.pz.KKBus.Model.Repositories.RewardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RewardManager {

    private final RewardRepo rewardRepo;

    @Autowired
    public RewardManager(RewardRepo rewardRepo) {
        this.rewardRepo = rewardRepo;
    }

    public List<Reward> findAll(){
        return rewardRepo.findAll();
    }

    public Optional<Reward> findByName(String name){
        return rewardRepo.findByName(name);
    }

    public Reward save(Reward reward, Optional<Customer> customer){
        reward.setCustomer(customer.get());
        reward.setRewardStatus(RewardStatus.Realized);
        reward.setDate(LocalDate.now());
        return rewardRepo.save(reward);
    }

    public Optional<Reward> deleteById(Long id){
        Optional<Reward> deleted = rewardRepo.findById(id);
        rewardRepo.deleteById(id);

        return deleted;
    }
}
