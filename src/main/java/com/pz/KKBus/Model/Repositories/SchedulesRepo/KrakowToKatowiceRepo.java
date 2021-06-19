package com.pz.KKBus.Model.Repositories.SchedulesRepo;

import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KrakowToKatowiceRepo extends JpaRepository<KrakowToKatowice, Long> {

    Optional<KrakowToKatowice> findByStop(String stop);
}
