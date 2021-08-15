package com.pz.KKBus.Customer.Model.Repositories.SchedulesRepo;

import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakowDeparture;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;


@Repository
public interface KrakowToKatowiceDepartureRepo extends JpaRepository<KrakowToKatowiceDeparture, Long> {

    List<KrakowToKatowiceDeparture> findByKrakowToKatowice(KrakowToKatowice krakowToKatowice);

    KrakowToKatowiceDeparture findTopByOrderBySatSunDepartureAsc();

    KrakowToKatowiceDeparture findTopByOrderByMonFriDepartureAsc();

    @Query(value = "SELECT * FROM KRAKOW_TO_KATOWICE_DEPARTURE  u WHERE mon_Fri_Departure = :time", nativeQuery = true)
    List<KrakowToKatowiceDeparture> findByMonFriDepartureContains(LocalTime time);

    @Query(value = "SELECT * FROM KRAKOW_TO_KATOWICE_DEPARTURE  u WHERE Sat_Sun_Departure = :time", nativeQuery = true)
    List<KrakowToKatowiceDeparture> findBySatSunDepartureContains(LocalTime time);
}
