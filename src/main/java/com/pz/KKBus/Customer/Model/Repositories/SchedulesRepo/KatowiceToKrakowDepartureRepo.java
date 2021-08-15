package com.pz.KKBus.Customer.Model.Repositories.SchedulesRepo;

import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakow;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakowDeparture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface KatowiceToKrakowDepartureRepo extends JpaRepository<KatowiceToKrakowDeparture, Long> {

    List<KatowiceToKrakowDeparture> findByKatowiceToKrakow(KatowiceToKrakow katowiceToKrakow);

    KatowiceToKrakowDeparture findTopByOrderBySatSunDepartureAsc();

    KatowiceToKrakowDeparture findTopByOrderByMonFriDepartureAsc();

    @Query(value = "SELECT * FROM KATOWICE_TO_KRAKOW_DEPARTURE  u WHERE mon_Fri_Departure = :time", nativeQuery = true)
    List<KatowiceToKrakowDeparture> findByMonFriDepartureContains(LocalTime time);

    @Query(value = "SELECT * FROM KATOWICE_TO_KRAKOW_DEPARTURE  u WHERE Sat_Sun_Departure = :time", nativeQuery = true)
    List<KatowiceToKrakowDeparture> findBySatSunDepartureContains(LocalTime time);
}
