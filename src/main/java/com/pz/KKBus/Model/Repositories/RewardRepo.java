package com.pz.KKBus.Model.Repositories;

import com.pz.KKBus.Model.Entites.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardRepo extends JpaRepository<Reward, Long> {

    Optional<Reward> findByName(String name);
}
