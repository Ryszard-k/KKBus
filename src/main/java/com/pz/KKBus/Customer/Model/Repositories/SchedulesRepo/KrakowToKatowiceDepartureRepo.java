package com.pz.KKBus.Customer.Model.Repositories.SchedulesRepo;

import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface KrakowToKatowiceDepartureRepo extends JpaRepository<KrakowToKatowiceDeparture, Long> {

    List<KrakowToKatowiceDeparture> findByKrakowToKatowice(KrakowToKatowice krakowToKatowice);

    KrakowToKatowiceDeparture findTopByOrderBySatSunDepartureAsc();

    KrakowToKatowiceDeparture findTopByOrderByMonFriDepartureAsc();
}
