package com.pz.KKBus.Manager.Schedules;

import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowice;
import com.pz.KKBus.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import com.pz.KKBus.Model.Repositories.SchedulesRepo.KrakowToKatowiceDepartureRepo;
import com.pz.KKBus.Model.Repositories.SchedulesRepo.KrakowToKatowiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
