package com.pz.KKBus.Manager.Schedules;

import com.pz.KKBus.Model.Entites.Schedules.KatowiceToKrakow;
import com.pz.KKBus.Model.Entites.Schedules.KatowiceToKrakowDeparture;
import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowice;
import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import com.pz.KKBus.Model.Repositories.SchedulesRepo.KatowiceToKrakowDepartureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class KatowiceToKrakowDepartureManager {

    private KatowiceToKrakowDepartureRepo katowiceToKrakowDepartureRepo;
    private KatowiceToKrakowManager katowiceToKrakowManager;

    @Autowired
    public KatowiceToKrakowDepartureManager(KatowiceToKrakowDepartureRepo katowiceToKrakowDepartureRepo, KatowiceToKrakowManager katowiceToKrakowManager) {
        this.katowiceToKrakowDepartureRepo = katowiceToKrakowDepartureRepo;
        this.katowiceToKrakowManager = katowiceToKrakowManager;
    }

    public List<KatowiceToKrakowDeparture> findAllFromKatowiceToKrakowDeparture(){
        return katowiceToKrakowDepartureRepo.findAll();
    }

    public List<KatowiceToKrakowDeparture> findByStopFromKatowiceToKrakowDeparture(String stop){
        Optional<KatowiceToKrakow> founds = katowiceToKrakowManager.findByStopFromKatowiceToKrakow(stop);
        return katowiceToKrakowDepartureRepo.findByKatowiceToKrakow(founds.get());
    }

    public Optional<KatowiceToKrakowDeparture> findByIdFromKatowiceToKrakowDeparture(Long id){
        return katowiceToKrakowDepartureRepo.findById(id);
    }

    public KatowiceToKrakowDeparture saveInKatowiceToKrakowDeparture(
            KatowiceToKrakowDeparture katowiceToKrakowDeparture,
            Optional<KatowiceToKrakow> katowiceToKrakow){

        katowiceToKrakowDeparture.setKatowiceToKrakow(katowiceToKrakow.get());
        return katowiceToKrakowDepartureRepo.save(katowiceToKrakowDeparture);
    }

    public KatowiceToKrakowDeparture updateToKatowiceToKrakowDeparture(
            KatowiceToKrakowDeparture katowiceToKrakowDeparture, Long id,
            KatowiceToKrakow katowiceToKrakow){

        katowiceToKrakowDeparture.setId(id);
        katowiceToKrakowDeparture.setKatowiceToKrakow(katowiceToKrakow);
        return katowiceToKrakowDepartureRepo.save(katowiceToKrakowDeparture);
    }

    public Optional<KatowiceToKrakowDeparture> deleteFromKatowiceToKrakowDeparture(
            Optional<KatowiceToKrakowDeparture> katowiceToKrakowDeparture){
        katowiceToKrakowDepartureRepo.delete(katowiceToKrakowDeparture.get());
        return katowiceToKrakowDeparture;
    }

}
