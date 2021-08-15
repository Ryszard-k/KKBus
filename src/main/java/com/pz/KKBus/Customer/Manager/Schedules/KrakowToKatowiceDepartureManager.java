package com.pz.KKBus.Customer.Manager.Schedules;

import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakowDeparture;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import com.pz.KKBus.Customer.Model.Repositories.SchedulesRepo.KrakowToKatowiceDepartureRepo;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class KrakowToKatowiceDepartureManager {

    private KrakowToKatowiceDepartureRepo krakowToKatowiceDepartureRepo;
    private KrakowToKatowiceManager krakowToKatowiceManager;

    @Autowired
    public KrakowToKatowiceDepartureManager(KrakowToKatowiceDepartureRepo krakowToKatowiceDepartureRepo, KrakowToKatowiceManager krakowToKatowiceManager) {
        this.krakowToKatowiceDepartureRepo = krakowToKatowiceDepartureRepo;
        this.krakowToKatowiceManager = krakowToKatowiceManager;
    }

    public List<KrakowToKatowiceDeparture> findAllFromKrakowToKatowiceDeparture(){
        return krakowToKatowiceDepartureRepo.findAll();
    }

    public List<KrakowToKatowiceDeparture> findByStopFromKrakowToKatowiceDeparture(String stop){
        Optional<KrakowToKatowice> founds = krakowToKatowiceManager.findByStopFromKrakowToKatowice(stop);
        return krakowToKatowiceDepartureRepo.findByKrakowToKatowice(founds.get());
    }

    public Optional<KrakowToKatowiceDeparture> findByIdFromKrakowToKatowiceDeparture(Long id){
        return krakowToKatowiceDepartureRepo.findById(id);
    }

    public List<KrakowToKatowiceDeparture> findByMonFriDepartureContains(LocalTime time){
        return krakowToKatowiceDepartureRepo.findByMonFriDepartureContains(time);
    }

    public List<KrakowToKatowiceDeparture> findBySatSunDepartureContains(LocalTime time){
        return krakowToKatowiceDepartureRepo.findBySatSunDepartureContains(time);
    }

    public KrakowToKatowiceDeparture saveInKrakowToKatowiceDeparture(
            KrakowToKatowiceDeparture krakowToKatowiceDeparture, Optional<KrakowToKatowice> krakowToKatowice){

        krakowToKatowiceDeparture.setKrakowToKatowice(krakowToKatowice.get());
        return krakowToKatowiceDepartureRepo.save(krakowToKatowiceDeparture);
    }

    public KrakowToKatowiceDeparture updateToKrakowToKatowiceDeparture(
            KrakowToKatowiceDeparture krakowToKatowiceDeparture, Long id, KrakowToKatowice krakowToKatowice){
        krakowToKatowiceDeparture.setId(id);
        krakowToKatowiceDeparture.setKrakowToKatowice(krakowToKatowice);
        return krakowToKatowiceDepartureRepo.save(krakowToKatowiceDeparture);
    }

    public Optional<KrakowToKatowiceDeparture> deleteFromKrakowToKatowiceDeparture(
            Optional<KrakowToKatowiceDeparture> krakowToKatowiceDeparture){
        krakowToKatowiceDepartureRepo.delete(krakowToKatowiceDeparture.get());
        return krakowToKatowiceDeparture;
    }
}
