package com.pz.KKBus.Model.Repositories.SchedulesRepo;

import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowice;
import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface KrakowToKatowiceDepartureRepo extends JpaRepository<KrakowToKatowiceDeparture, Long> {

    List<KrakowToKatowiceDeparture> findByKrakowToKatowice(KrakowToKatowice krakowToKatowice);

    KrakowToKatowiceDeparture findTopByOrderBySatSunDepartureAsc();

    KrakowToKatowiceDeparture findTopByOrderByMonFriDepartureAsc();
}
