package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Staff.Model.Entites.Courses.Report;
import com.pz.KKBus.Staff.Model.Entites.Courses.StopPassengersPair;
import com.pz.KKBus.Staff.Model.Repositories.Courses.StopPassengersPairRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StopPassengersPairManager {

    private final StopPassengersPairRepo stopPassengersPairRepo;

    @Autowired
    public StopPassengersPairManager(StopPassengersPairRepo stopPassengersPairRepo) {
        this.stopPassengersPairRepo = stopPassengersPairRepo;
    }

    public StopPassengersPair save(StopPassengersPair StopPassengersPair) {
        return stopPassengersPairRepo.save(StopPassengersPair);
    }
}
