package com.pz.KKBus.Customer.Manager.Schedules;

import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakow;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakowDeparture;
import com.pz.KKBus.Customer.Model.Repositories.SchedulesRepo.KatowiceToKrakowDepartureRepo;
import com.pz.KKBus.Customer.Model.Repositories.SchedulesRepo.KatowiceToKrakowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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

    @EventListener(ApplicationReadyEvent.class)
    public void fillKatowiceToKrakow(){
        katowiceToKrakowRepo.save(new KatowiceToKrakow((long) 1,"Przystanek1",null
                , 0, 0));
        katowiceToKrakowRepo.save(new KatowiceToKrakow((long) 2,"Przystanek2",null
                , 7, 32));
        katowiceToKrakowRepo.save(new KatowiceToKrakow((long) 3,"Przystanek3",null
                , 12, 62));
        katowiceToKrakowRepo.save(new KatowiceToKrakow((long) 4,"Przystanek4",null
                , 17, 81));

        katowiceToKrakowDepartureRepo.save(new KatowiceToKrakowDeparture((long) 1, LocalTime.parse("08:01"),
                LocalTime.parse("11:47"), findByIdFromKatowiceToKrakow(1L).get()));
        katowiceToKrakowDepartureRepo.save(new KatowiceToKrakowDeparture((long) 2, LocalTime.parse("13:01"),
                LocalTime.parse("16:47"), findByIdFromKatowiceToKrakow(1L).get()));

        katowiceToKrakowDepartureRepo.save(new KatowiceToKrakowDeparture((long) 3, LocalTime.parse("08:30"),
                LocalTime.parse("11:47"), findByIdFromKatowiceToKrakow(2L).get()));
        katowiceToKrakowDepartureRepo.save(new KatowiceToKrakowDeparture((long) 4, LocalTime.parse("13:51"),
                LocalTime.parse("17:47"), findByIdFromKatowiceToKrakow(2L).get()));

        katowiceToKrakowDepartureRepo.save(new KatowiceToKrakowDeparture((long) 5, LocalTime.parse("08:01"),
                LocalTime.parse("09:47"), findByIdFromKatowiceToKrakow(3L).get()));
        katowiceToKrakowDepartureRepo.save(new KatowiceToKrakowDeparture((long) 6, LocalTime.parse("13:01"),
                LocalTime.parse("16:47"), findByIdFromKatowiceToKrakow(3L).get()));
    }
}
