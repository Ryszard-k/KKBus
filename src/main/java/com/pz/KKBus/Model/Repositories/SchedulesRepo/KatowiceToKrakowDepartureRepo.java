package com.pz.KKBus.Model.Repositories.SchedulesRepo;

import com.pz.KKBus.Model.Entites.Schedules.KatowiceToKrakow;
import com.pz.KKBus.Model.Entites.Schedules.KatowiceToKrakowDeparture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KatowiceToKrakowDepartureRepo extends JpaRepository<KatowiceToKrakowDeparture, Long> {

    List<KatowiceToKrakowDeparture> findByKatowiceToKrakow(KatowiceToKrakow katowiceToKrakow);
}
