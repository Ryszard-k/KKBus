package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Customer.Model.Entites.Customer;
import com.pz.KKBus.Customer.Model.Entites.Reward;
import com.pz.KKBus.Customer.Model.Enums.RewardStatus;
import com.pz.KKBus.Staff.Model.Entites.Availability;
import com.pz.KKBus.Staff.Model.Repositories.AvailabilityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AvailabilityManager {

    private final AvailabilityRepo availabilityRepo;

    @Autowired
    public AvailabilityManager(AvailabilityRepo availabilityRepo) {
        this.availabilityRepo = availabilityRepo;
    }

    public List<Availability> findAll(){
        return availabilityRepo.findAll();
    }

    public Optional<Availability> findById(Long id){
        return availabilityRepo.findById(id);
    }

    public List<Availability> saveAll(List<Availability> availability){
        return availabilityRepo.saveAll(availability);
    }

    public List<Availability> deleteAll(List<Availability> deleted){
        deleted.removeIf(availability -> !availabilityRepo.existsById(availability.getId()));
        availabilityRepo.deleteAll(deleted);
        return deleted;
    }
}
