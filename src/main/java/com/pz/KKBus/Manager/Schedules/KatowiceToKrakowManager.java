package com.pz.KKBus.Manager.Schedules;

import com.pz.KKBus.Model.Entites.Schedules.KatowiceToKrakow;
import com.pz.KKBus.Model.Entites.Schedules.KatowiceToKrakowDeparture;
import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowice;
import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import com.pz.KKBus.Model.Repositories.SchedulesRepo.KatowiceToKrakowDepartureRepo;
import com.pz.KKBus.Model.Repositories.SchedulesRepo.KatowiceToKrakowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KatowiceToKrakowManager {

    private KatowiceToKrakowRepo katowiceToKrakowRepo;
    private KatowiceToKrakowDepartureRepo katowiceToKrakowDepartureRepo;

    @Autowired
    public KatowiceToKrakowManager(KatowiceToKrakowRepo katowiceToKrakowRepo, KatowiceToKrakowDepartureRepo katowiceToKrakowDepartureRepo) {
        this.katowiceToKrakowRepo = katowiceToKrakowRepo;
        this.katowiceToKrakowDepartureRepo = katowiceToKrakowDepartureRepo;
    }

    public Iterable<KatowiceToKrakow> findAllFromKatowiceToKrakow(){
        return katowiceToKrakowRepo.findAll();
    }

    public Optional<KatowiceToKrakow> findByStopFromKatowiceToKrakow(String stop){
        return katowiceToKrakowRepo.findByStop(stop);
    }

    public Optional<KatowiceToKrakow> findByIdFromKatowiceToKrakow(Long id){
        return katowiceToKrakowRepo.findById(id);
    }

    public KatowiceToKrakow saveInKatowiceToKrakow(KatowiceToKrakow katowiceToKrakow){
        Set<KatowiceToKrakowDeparture> set2 = katowiceToKrakow.getKatowiceToKrakowDeparture().stream()
                .map(temp -> {
                    temp.setKatowiceToKrakow(katowiceToKrakow);
                    return temp;
                }).collect(Collectors.toSet());

        katowiceToKrakow.setKatowiceToKrakowDeparture(set2);
        return katowiceToKrakowRepo.save(katowiceToKrakow);
    }

    public KatowiceToKrakow updateToKatowiceToKrakow(KatowiceToKrakow katowiceToKrakow,
                                                     Optional<KatowiceToKrakow> katowiceToKrakow1){
        Set<KatowiceToKrakowDeparture> set2 = katowiceToKrakow.getKatowiceToKrakowDeparture().stream()
                .map(temp -> {
                    temp.setKatowiceToKrakow(katowiceToKrakow);
                    return temp;
                }).collect(Collectors.toSet());

        katowiceToKrakow.setId(katowiceToKrakow1.get().getId());
        katowiceToKrakow.setKatowiceToKrakowDeparture(set2);
        return katowiceToKrakowRepo.save(katowiceToKrakow);
    }

    public Optional<KatowiceToKrakow> deleteFromKatowiceToKrakow(Optional<KatowiceToKrakow> katowiceToKrakow){
        katowiceToKrakowRepo.delete(katowiceToKrakow.get());
        return katowiceToKrakow;
    }
}
