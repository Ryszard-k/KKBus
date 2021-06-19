package com.pz.KKBus.Model.Repositories.SchedulesRepo;

import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KrakowToKatowiceDepartureRepo extends JpaRepository<KrakowToKatowiceDeparture, Long> {
}
