package com.pz.KKBus.Customer.Model.Repositories.SchedulesRepo;

import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KatowiceToKrakowRepo extends JpaRepository<KatowiceToKrakow, Long> {

    Optional<KatowiceToKrakow> findByStop(String stop);
}
