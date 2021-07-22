package com.pz.KKBus.Customer.Model.Repositories;

import com.pz.KKBus.Customer.Model.Entites.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardRepo extends JpaRepository<Reward, Long> {

    Optional<Reward> findByName(String name);
}
