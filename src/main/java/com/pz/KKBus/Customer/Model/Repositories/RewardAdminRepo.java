package com.pz.KKBus.Customer.Model.Repositories;

import com.pz.KKBus.Customer.Model.Entites.RewardAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardAdminRepo extends JpaRepository<RewardAdmin, Long> {

    Optional<RewardAdmin> findByName(String name);
}
