package com.pz.KKBus.Customer.Manager;

import com.pz.KKBus.Customer.Model.Entites.Reward;
import com.pz.KKBus.Customer.Model.Enums.RewardStatus;
import com.pz.KKBus.Customer.Model.Repositories.RewardRepo;
import com.pz.KKBus.Customer.Model.Entites.Customer;
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

    public Optional<Reward> findById(Long id){
        return rewardRepo.findById(id);
    }

    public Reward save(Reward reward, Optional<Customer> customer){
        reward.setCustomer(customer.get());
        reward.setRewardStatus(RewardStatus.Unrealized);
        reward.setDate(LocalDate.now());
        return rewardRepo.save(reward);
    }

    public Optional<Reward> deleteById(Long id){
        Optional<Reward> deleted = rewardRepo.findById(id);
        rewardRepo.deleteById(id);

        return deleted;
    }
}
