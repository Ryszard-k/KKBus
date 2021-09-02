package com.pz.KKBus.Customer.Manager;

import com.pz.KKBus.Customer.Model.Entites.Customer;
import com.pz.KKBus.Customer.Model.Entites.Reward;
import com.pz.KKBus.Customer.Model.Entites.RewardAdmin;
import com.pz.KKBus.Customer.Model.Enums.RewardStatus;
import com.pz.KKBus.Customer.Model.Repositories.RewardAdminRepo;
import com.pz.KKBus.Customer.Model.Repositories.RewardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RewardAdminManager {

    private final RewardAdminRepo rewardAdminRepo;

    @Autowired
    public RewardAdminManager(RewardAdminRepo rewardAdminRepo) {
        this.rewardAdminRepo = rewardAdminRepo;
    }

    public List<RewardAdmin> findAll(){
        return rewardAdminRepo.findAll();
    }

    public Optional<RewardAdmin> findByName(String name){
        return rewardAdminRepo.findByName(name);
    }

    public Optional<RewardAdmin> findById(Long id){
        return rewardAdminRepo.findById(id);
    }

    public RewardAdmin save(RewardAdmin rewardAdmin){
        return rewardAdminRepo.save(rewardAdmin);
    }

    public Optional<RewardAdmin> deleteById(Long id){
        Optional<RewardAdmin> deleted = rewardAdminRepo.findById(id);
        rewardAdminRepo.deleteById(id);

        return deleted;
    }
}
