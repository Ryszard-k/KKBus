package com.pz.KKBus.Customer.Manager.Schedules;

import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import com.pz.KKBus.Customer.Model.Repositories.SchedulesRepo.KrakowToKatowiceDepartureRepo;
import com.pz.KKBus.Customer.Model.Repositories.SchedulesRepo.KrakowToKatowiceRepo;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KrakowToKatowiceManager {

    private KrakowToKatowiceRepo krakowToKatowiceRepo;
    private KrakowToKatowiceDepartureRepo krakowToKatowiceDepartureRepo;

    @Autowired
    public KrakowToKatowiceManager(KrakowToKatowiceRepo krakowToKatowiceRepo, KrakowToKatowiceDepartureRepo krakowToKatowiceDepartureRepo) {
        this.krakowToKatowiceRepo = krakowToKatowiceRepo;
        this.krakowToKatowiceDepartureRepo = krakowToKatowiceDepartureRepo;
    }

    public Iterable<KrakowToKatowice> findAllFromKrakowToKatowice(){
        return krakowToKatowiceRepo.findAll();
    }

    public Optional<KrakowToKatowice> findByStopFromKrakowToKatowice(String stop){
        return krakowToKatowiceRepo.findByStop(stop);
    }

    public Optional<KrakowToKatowice> findByIdFromKrakowToKatowice(Long id){
        return krakowToKatowiceRepo.findById(id);
    }

    public KrakowToKatowice saveInKrakowToKatowice(KrakowToKatowice krakowToKatowice){
        Set<KrakowToKatowiceDeparture> set2 = krakowToKatowice.getKrakowToKatowiceDeparture().stream()
                .map(temp -> {
                    temp.setKrakowToKatowice(krakowToKatowice);
                    return temp;
                }).collect(Collectors.toSet());

        krakowToKatowice.setKrakowToKatowiceDeparture(set2);
        return krakowToKatowiceRepo.save(krakowToKatowice);
    }

    public KrakowToKatowice updateToKrakowToKatowice(KrakowToKatowice krakowToKatowice,
                                                     Optional<KrakowToKatowice> krakowToKatowice1){
        Set<KrakowToKatowiceDeparture> set2 = krakowToKatowice1.get().getKrakowToKatowiceDeparture().stream()
                .map(temp -> {
                    temp.setKrakowToKatowice(krakowToKatowice);
                    return temp;
                }).collect(Collectors.toSet());

        krakowToKatowice.setId(krakowToKatowice1.get().getId());
        krakowToKatowice.setKrakowToKatowiceDeparture(set2);
        return krakowToKatowiceRepo.save(krakowToKatowice);
    }

    public Optional<KrakowToKatowice> deleteFromKrakowToKatowice(Optional<KrakowToKatowice> krakowToKatowice){
        krakowToKatowiceRepo.delete(krakowToKatowice.get());
        return krakowToKatowice;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillKrakowToKatowice() {
        krakowToKatowiceRepo.save(new KrakowToKatowice((long) 1, "Przystanek1", null
                , 0, 0));
        krakowToKatowiceRepo.save(new KrakowToKatowice((long) 2, "Przystanek2", null
                , 10, 40));
        krakowToKatowiceRepo.save(new KrakowToKatowice((long) 3, "Przystanek3", null
                , 17, 80));

        krakowToKatowiceDepartureRepo.save(new KrakowToKatowiceDeparture((long) 1, LocalTime.parse("08:01"),
                LocalTime.parse("11:47"), findByIdFromKrakowToKatowice(1L).get()));
        krakowToKatowiceDepartureRepo.save(new KrakowToKatowiceDeparture((long) 2, LocalTime.parse("13:01"),
                LocalTime.parse("16:47"), findByIdFromKrakowToKatowice(1L).get()));

        krakowToKatowiceDepartureRepo.save(new KrakowToKatowiceDeparture((long) 3, LocalTime.parse("08:30"),
                LocalTime.parse("11:47"), findByIdFromKrakowToKatowice(2L).get()));
        krakowToKatowiceDepartureRepo.save(new KrakowToKatowiceDeparture((long) 4, LocalTime.parse("13:51"),
                LocalTime.parse("17:47"), findByIdFromKrakowToKatowice(2L).get()));

        krakowToKatowiceDepartureRepo.save(new KrakowToKatowiceDeparture((long) 5, LocalTime.parse("08:01"),
                LocalTime.parse("09:47"), findByIdFromKrakowToKatowice(3L).get()));
        krakowToKatowiceDepartureRepo.save(new KrakowToKatowiceDeparture((long) 6, LocalTime.parse("13:01"),
                LocalTime.parse("16:47"), findByIdFromKrakowToKatowice(3L).get()));
    }
}
