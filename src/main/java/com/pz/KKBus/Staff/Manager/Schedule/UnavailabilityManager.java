package com.pz.KKBus.Staff.Manager.Schedule;

import com.pz.KKBus.Staff.Model.Entites.Schedule.Unavailability;
import com.pz.KKBus.Staff.Model.Repositories.Schedule.UnavailabilityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnavailabilityManager {

    private final UnavailabilityRepo unavailabilityRepo;

    @Autowired
    public UnavailabilityManager(UnavailabilityRepo unavailabilityRepo) {
        this.unavailabilityRepo = unavailabilityRepo;
    }

    public List<Unavailability> findAll(){
        return unavailabilityRepo.findAll();
    }

    public Optional<Unavailability> findById(Long id){
        return unavailabilityRepo.findById(id);
    }

    public List<Unavailability> saveAll(List<Unavailability> unavailabilities){
        return unavailabilityRepo.saveAll(unavailabilities);
    }

    public List<Unavailability> deleteAll(List<Unavailability> deleted){
        deleted.removeIf(unavailability -> !unavailabilityRepo.existsById(unavailability.getId()));
        unavailabilityRepo.deleteAll(deleted);
        return deleted;
    }
}
