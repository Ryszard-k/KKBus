package com.pz.KKBus.Customer.Model.Repositories.SchedulesRepo;

import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakow;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakowDeparture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KatowiceToKrakowDepartureRepo extends JpaRepository<KatowiceToKrakowDeparture, Long> {

    List<KatowiceToKrakowDeparture> findByKatowiceToKrakow(KatowiceToKrakow katowiceToKrakow);

    KatowiceToKrakowDeparture findTopByOrderBySatSunDepartureAsc();

    KatowiceToKrakowDeparture findTopByOrderByMonFriDepartureAsc();
}
